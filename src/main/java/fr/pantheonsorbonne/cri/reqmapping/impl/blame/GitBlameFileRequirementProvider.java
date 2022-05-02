package fr.pantheonsorbonne.cri.reqmapping.impl.blame;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.impl.FileRequirementMappingProvider;
import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class GitBlameFileRequirementProvider extends VoidVisitorAdapter<Void>
        implements FileRequirementMappingProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitBlameFileRequirementProvider.class);
    String sourceRootDir;
    Repository repo;


    @Inject
    public GitBlameFileRequirementProvider(@Named("sourceRootDir") String sourceRootDir, Repository repo) {

        this.sourceRootDir = sourceRootDir;
        this.repo = repo;
    }

    @Override
    public Collection<ReqMatch> getReqMatcher(Path p) {

        try {
            Collection<ReqMatch> res = blameUnsafeVisit(p);
            return res;
        } catch (GitAPIException | IOException e) {

            LOGGER.error("fatal error " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    private Collection<ReqMatch> blameUnsafeVisit(Path file) throws GitAPIException, IOException {

        BlameDataWrapper wrapper = new BlameDataWrapper();

        File relativeFilePath = this.repo.getDirectory().getParentFile().toPath().relativize(file).toFile();
        if (Files.isRegularFile(file)
                && com.google.common.io.Files.getFileExtension(relativeFilePath.toString()).equals("java")) {

            BlameCommand blamer = new BlameCommand(this.repo);
            ObjectId commitID = this.repo.resolve("HEAD");
            blamer.setStartCommit(commitID);

            blamer.setFilePath(relativeFilePath.getPath());
            BlameResult blamed = blamer.call();

            int lines = countLinesOfFileInCommit(this.repo, commitID, relativeFilePath.toString());
            final Pattern p = Pattern.compile(".*#([0-9]+)");
            Map<Integer, Collection<String>> fileBlameData = new HashMap<>();
            for (int i = 0; i < lines; i++) {
                RevCommit commit = blamed.getSourceCommit(i);
                Matcher m = p.matcher(commit.getShortMessage());
                if (m.matches()) {
                    Collection<String> matchingIssueId = new HashSet<>();
                    for (int j = 1; j < m.groupCount() + 1; j++) {
                        matchingIssueId.add(m.group(j));
                    }
                    fileBlameData.put(i, matchingIssueId);
                }

            }

            String inferedClassName = Paths.get(sourceRootDir).relativize(relativeFilePath.toPath())
                    .toString().replaceAll("/", ".").replaceFirst("[.][^.]+$", "");
            wrapper.blameData.put(inferedClassName, fileBlameData);

            JavaParser parser = new JavaParser();

            Optional<CompilationUnit> cu = parser.parse(file.toFile()).getResult();

            if (cu.isPresent()) {

                ReqMatcherJavaVisitor blameVisitor = new ReqMatcherJavaVisitor();

                cu.get().accept(blameVisitor, wrapper);
                return blameVisitor.getMatchers();

            }

        }
        return Collections.EMPTY_LIST;
    }

    private static int countLinesOfFileInCommit(Repository repository, ObjectId commitID, String name)
            throws IOException {
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
                ByteArrayOutputStream stream = new ByteArrayOutputStream();
                loader.copyTo(stream);

                revWalk.dispose();

                return CharStreams.readLines(CharBuffer.wrap(stream.toString().toCharArray())).size();
            }
        }
    }

}