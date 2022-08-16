package fr.pantheonsorbonne.cri.reqmapping.impl.blame;

import com.github.javaparser.JavaParser;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.io.CharStreams;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.Utils;
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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.CharBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class GitBlameFileRequirementProvider extends VoidVisitorAdapter<Void>
        implements FileRequirementMappingProvider {
    private static final Logger LOGGER = LoggerFactory.getLogger(GitBlameFileRequirementProvider.class);
    final Repository repo;
    final Boolean doMethods;
    final Boolean doInstructions;
    final List<String> sourceRootDirs;

    @Inject
    public GitBlameFileRequirementProvider(@Named("sourceRootDir") List<String> sourceRootDirs, Repository repo, @Named("DoInstructionsDiff")
            Boolean doInstructions, @Named("DoMethodsDiff")
                                                   Boolean doMethods) {
        this.doMethods = doMethods;
        this.doInstructions = doInstructions;
        this.repo = repo;
        this.sourceRootDirs = sourceRootDirs;
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

        Collection<ReqMatch> res = new ArrayList<>();


        File relativeFilePath = this.repo.getDirectory().getParentFile().toPath().relativize(file).toFile();
        if (Files.isRegularFile(file)
                && com.google.common.io.Files.getFileExtension(relativeFilePath.toString()).equals("java")) {

            BlameCommand blamer = new BlameCommand(this.repo);
            ObjectId commitID = this.repo.resolve("HEAD");
            blamer.setStartCommit(commitID);

            blamer.setFilePath(relativeFilePath.getPath());
            BlameResult blamed = blamer.call();

            int lines = countLinesOfFileInCommit(this.repo, commitID, relativeFilePath.toString());

            Map<Integer, Collection<String>> fileBlameData = new HashMap<>();
            String inferedClassName = Paths.get(sourceRootDirs.get(0)).relativize(relativeFilePath.toPath()).toString().replaceAll("/", ".").replaceFirst("[.][^.]+$", "");


            if (doInstructions) {
                res.addAll(extractLineReqMatchers(blamed, lines, inferedClassName));
            }
            if (doMethods) {
                for (int idx = 0; idx < lines; idx++) {
                    RevCommit commit = blamed.getSourceCommit(idx);

                    fileBlameData.put(idx, Utils.getIssueIdFromCommits(commit.getFullMessage()));
                }
                res.addAll(extractMethodDeclaration(file, fileBlameData, inferedClassName));
            }
            return res;

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

    private Collection<ReqMatch> extractLineReqMatchers(BlameResult blamed, int lines, String inferedClassName) {
        Collection<ReqMatch> res = new ArrayList<>();
        for (int idx = 0; idx < lines; idx++) {
            RevCommit commit = blamed.getSourceCommit(idx);
            var commits = Utils.getIssueIdFromCommits(commit.getFullMessage());
            if (commits.size() > 0) {
                res.add(
                        ReqMatch.builder()
                                .fQClassName(inferedClassName)
                                .line(blamed.getSourceLine(idx))
                                .commits(commits)
                                .build());
            }
        }
        return res;
    }

    private Collection<ReqMatch> extractMethodDeclaration(Path file, Map<Integer, Collection<String>> fileBlameData, String inferedClassName) throws FileNotFoundException {
        BlameDataWrapper wrapper = new BlameDataWrapper();
        Collection<ReqMatch> res = new ArrayList<>();
        wrapper.blameData.put(inferedClassName, fileBlameData);

        JavaParser parser = new JavaParser();

        Optional<CompilationUnit> cu = parser.parse(file.toFile()).getResult();

        if (cu.isPresent()) {

            BlameMethodDeclarationVisitor blameVisitor = new BlameMethodDeclarationVisitor();

            cu.get().accept(blameVisitor, wrapper);
            res.addAll(blameVisitor.getMatchers());

        }
        return res;
    }

}