package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

import com.google.inject.Inject;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
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

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class GumTreeFileRequirementMappingProvider implements FileRequirementMappingProvider {

    private final GumTreeFacade facade = new GumTreeFacade();
    @Inject
    private Repository repo;
    @Inject
    private Git git;

    @Override
    public Collection<ReqMatch> getReqMatcher(Path p) {

        try {
            List<Diff> diffs = this.materializeCommitDiff(p);
            return this.facade.getReqMatcher(diffs);
        } catch (IOException | GitAPIException e) {
            e.printStackTrace();
            System.exit(-3);
            throw new RuntimeException("unreachable");
        }

    }

    private List<Diff> materializeCommitDiff(Path file) throws GitAPIException, IOException {

        List<Diff> diffs = new ArrayList<>();
        File relativeFilePath = this.repo.getDirectory().getParentFile().toPath().relativize(file).toFile();
        if (Files.isRegularFile(file)
                && com.google.common.io.Files.getFileExtension(relativeFilePath.toString()).equals("java")) {

            LogCommand logCommand = git.log().add(this.repo.resolve(Constants.HEAD))
                    .addPath(relativeFilePath.toString());

            List<CommitIssueMapping> commitIssueMappings = new ArrayList<>();
            for (RevCommit revCommit : logCommand.call()) {

                CommitIssueMapping mapping = new CommitIssueMapping();
                mapping.id = revCommit;
                mapping.issueId = getIssueIdFromCommits(revCommit.getFullMessage());
                commitIssueMappings.add(mapping);

            }
            Diff.DiffBuilder builder = Diff.getBuilder();
            for (CommitIssueMapping mapping : commitIssueMappings) {
                Path path = materializeFileFromCommit(this.repo, mapping.id, relativeFilePath.toString());
                builder.add(path, mapping.issueId.stream().collect(Collectors.joining(" ")));

            }

            return builder.build();

        }

        throw new NoSuchFileException(file.toString());

    }

    private List<String> getIssueIdFromCommits(String message) {
        List<String> res = new ArrayList<>();
        final Pattern p = Pattern.compile("#([0-9]+)");
        Matcher m = p.matcher(message);
        while (m.find()) {
            res.add(m.group(1));
        }
        return res;
    }

    private Path materializeFileFromCommit(Repository repository, ObjectId commitID, String name) throws IOException {

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
                    throw new IllegalStateException("Did not find expected file ");
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
