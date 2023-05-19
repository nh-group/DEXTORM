package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.google.common.io.MoreFiles;
import com.google.common.io.RecursiveDeleteOption;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.Utils;
import fr.pantheonsorbonne.cri.reqmapping.exception.SkippedFileException;
import fr.pantheonsorbonne.cri.reqmapping.impl.FileRequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.gumTree.visitor.DiffCollector;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.diff.DiffEntry;
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
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

public class GumTreeFileRequirementMappingProvider implements FileRequirementMappingProvider {

    private static final Logger LOGGER = LoggerFactory.getLogger(GumTreeFileRequirementMappingProvider.class);
    private final GumTreeFacade facade;
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
    public GumTreeFileRequirementMappingProvider(@Named("repoAddress") String repoAddress, @Named("git-branch") String branchName, @Named("DoMethodsDiff") Boolean doMethods, @Named("DoInstructionsDiff") Boolean doInstructions, @Named("temp-git-repo") Path lookupFolderPath, @Named("useCache") Boolean useCache) {
        if (useCache) {
            this.facade = new CachedGumTreeFacade();
        } else {
            this.facade = new GumTreeFacade();
        }
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

    public void dispose() {
        try {
            MoreFiles.deleteRecursively(this.localBareRepoAddress.toPath(), RecursiveDeleteOption.ALLOW_INSECURE);
        } catch (IOException e) {
            // cleanup
        }
    }

    @Override
    public Collection<ReqMatch> getReqMatcher(Path p) {

        Git git = this.getFreshRepo();
        List<Diff> diffs = null;
        try {
            diffs = materializeCommitDiff(p, git, lookupFolderPath);
            return this.facade.getReqMatcher(this.lookupFolderPath.relativize(p).toString(), diffs, this.doMethods, this.doInstructions);
        } catch (SkippedFileException e) {
            LOGGER.info("we skipped " + p.toString() + " as per configuration");
            return Collections.EMPTY_LIST;
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            System.exit(-3);
            throw new RuntimeException("unreachable");


        } finally {
            try {
                MoreFiles.deleteRecursively(git.getRepository().getDirectory().toPath().getParent(), RecursiveDeleteOption.ALLOW_INSECURE);
            } catch (IOException e) {
                e.printStackTrace();
            }
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

    private static List<Diff> materializeCommitDiff(Path file, Git git, Path lookupFolderPath) throws GitAPIException, IOException, SkippedFileException {


        File relativeFilePath = lookupFolderPath.relativize(file).toFile();
        DiffCollector collector = new DiffCollector(git.getRepository());

        if (Files.isRegularFile(file)
                && com.google.common.io.Files.getFileExtension(relativeFilePath.toString()).equals("java")
                && !relativeFilePath.getName().equals("package-info.java")) {


            List<CommitIssueMapping> commitIssueMappings = new ArrayList<>();
            //start from head to the begining of time
            Deque<String> names = new LinkedList<>();
            List<RevCommit> revCommits = new ArrayList<>();
            collector.loadFileHistory(relativeFilePath.toString(), revCommits, names);
            for (RevCommit revCommit : revCommits) {

                CommitIssueMapping mapping = new CommitIssueMapping();
                mapping.commitId = revCommit;
                mapping.issueId = Utils.getIssueIdFromCommits(revCommit.getFullMessage());
                commitIssueMappings.add(mapping);

            }
            //we need to start from the begining of time toward HEAD, so we reverse the list
            Collections.reverse(commitIssueMappings);
            Diff.DiffBuilder builder = Diff.getBuilder();
            for (CommitIssueMapping mapping : commitIssueMappings) {
                Path path = null;
                do {
                    LOGGER.debug("mapping {} for file {}", mapping.commitId.getName(), names.peekLast());
                    path = materializeFileFromCommit(git.getRepository(), mapping.commitId, names.peekLast());
                }
                while (path == null && !names.isEmpty() && names.pollLast() != null);
                if (path != null) {
                    builder.add(path, mapping.issueId.stream().collect(Collectors.joining(" ")), mapping.commitId.name());
                }


            }
            MoreFiles.deleteDirectoryContents(git.getRepository().getDirectory().toPath(), RecursiveDeleteOption.ALLOW_INSECURE);

            try {
                return builder.build();
            } finally {
                builder.dispose();
            }

        }

        throw new SkippedFileException(file.toString());

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
