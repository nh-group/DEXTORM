package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.google.common.io.MoreFiles;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.Utils;
import fr.pantheonsorbonne.cri.reqmapping.impl.FileRequirementMappingProvider;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.LogCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Constants;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class GumTreeFileRequirementMappingProvider implements FileRequirementMappingProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GumTreeFileRequirementMappingProvider.class);
    private final GumTreeFacade facade = new GumTreeFacade();
    private final String repoAddress;
    @Named("temp-git-repo")
    Path lookupFolderPath;
    private File localBareRepoAddress;
    @Inject
    @Named("DoMethodsDiff")
    private Boolean doMethods;
    @Inject
    @Named("DoInstructionsDiff")
    private Boolean doInstructions;
    private String branchName;

    @Inject
    public GumTreeFileRequirementMappingProvider(@Named("repoAddress") String repoAddress, @Named("git-branch") String branchName, @Named("DoMethodsDiff") Boolean doMethods, @Named("DoInstructionsDiff") Boolean doInstructions, @Named("temp-git-repo") Path lookupFolderPath) {
        this.repoAddress = repoAddress;
        this.lookupFolderPath = lookupFolderPath;
        try {
            this.localBareRepoAddress = Files.createTempDirectory("dextorm").toFile();
            this.branchName = branchName;
            Git.cloneRepository().setURI(repoAddress).setBranch(branchName).setDirectory(localBareRepoAddress).setBare(true).call();
            this.doMethods = doMethods;
            this.doInstructions = doInstructions;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public Collection<ReqMatch> getReqMatcher(Path p) {

        Git git = this.getFreshRepo();
        try {
            List<Diff> diffs = materializeCommitDiff(p, git, lookupFolderPath);
            return this.facade.getReqMatcher(diffs, this.doMethods, this.doInstructions);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            System.exit(-3);
            throw new RuntimeException("unreachable");
        }

    }

    private Git getFreshRepo() {
        try {
            Path tmpRepo = Files.createTempDirectory("dextorm");
            Git git = Git.cloneRepository().setURI("file://" + this.localBareRepoAddress).setBranch(this.branchName).setDirectory(tmpRepo.toFile()).setBare(false).call();
            return git;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            System.exit(-1);
        }
        return null;//unreachable

    }

    private static List<Diff> materializeCommitDiff(Path file, Git git, Path lookupFolderPath) throws GitAPIException, IOException {

        File relativeFilePath = lookupFolderPath.relativize(file).toFile();
        if (Files.isRegularFile(file)
                && com.google.common.io.Files.getFileExtension(relativeFilePath.toString()).equals("java")) {

            LogCommand logCommand = git.log().add(git.getRepository().resolve(Constants.HEAD))
                    .addPath(relativeFilePath.toString());

            List<CommitIssueMapping> commitIssueMappings = new ArrayList<>();
            //start from head to the begining of time
            for (RevCommit revCommit : logCommand.call()) {

                CommitIssueMapping mapping = new CommitIssueMapping();
                mapping.id = revCommit;
                mapping.issueId = Utils.getIssueIdFromCommits(revCommit.getFullMessage());
                commitIssueMappings.add(mapping);

            }
            //we need to start from the begining of time toward HEAD, so we reverse the list
            Collections.reverse(commitIssueMappings);
            Diff.DiffBuilder builder = Diff.getBuilder();
            for (CommitIssueMapping mapping : commitIssueMappings) {
                LOGGER.debug("mapping {} for file {}", mapping.id.getName(), relativeFilePath);
                Path path = materializeFileFromCommit(git.getRepository(), mapping.id, relativeFilePath.toString());
                if (path != null) {
                    builder.add(path, mapping.issueId.stream().collect(Collectors.joining(" ")));
                }


            }
            MoreFiles.deleteDirectoryContents(git.getRepository().getDirectory().toPath());

            return builder.build();

        }

        throw new NoSuchFileException(file.toString());

    }

    private static Path materializeFileFromCommit(Repository repository, ObjectId commitID, String name) throws IOException {

        Path temp1 = Files.createTempFile(
                com.google.common.io.Files.getNameWithoutExtension(name), ".java");
        try (RevWalk revWalk = new RevWalk(repository)) {
            RevCommit commit = revWalk.parseCommit(commitID);
            RevTree tree = commit.getTree();


            // now try to find a specific file
            try (TreeWalk treeWalk = new TreeWalk(repository)) {
                treeWalk.addTree(tree);
                treeWalk.setRecursive(true);
                treeWalk.setFilter(PathFilter.create(name));
                if (!treeWalk.next()) {
                    LOGGER.warn("failed to find file {} in commit {}", name, commit.getName());
                    return null;
                }

                ObjectId objectId = treeWalk.getObjectId(0);
                ObjectLoader loader = repository.open(objectId);

                // load the content of the file into a stream

                FileOutputStream fos = new FileOutputStream(temp1.toFile());
                loader.copyTo(fos);

                revWalk.dispose();

            }
        }

        return temp1;
    }

}
