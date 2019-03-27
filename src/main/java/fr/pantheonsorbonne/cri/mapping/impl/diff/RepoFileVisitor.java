package fr.pantheonsorbonne.cri.mapping.impl.diff;

import java.nio.CharBuffer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.eclipse.jgit.api.BlameCommand;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.blame.BlameResult;
import org.eclipse.jgit.errors.AmbiguousObjectException;
import org.eclipse.jgit.errors.IncorrectObjectTypeException;
import org.eclipse.jgit.errors.MissingObjectException;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.ObjectId;
import org.eclipse.jgit.lib.ObjectLoader;
import org.eclipse.jgit.lib.Repository;
import org.eclipse.jgit.revwalk.RevCommit;
import org.eclipse.jgit.revwalk.RevTree;
import org.eclipse.jgit.revwalk.RevWalk;
import org.eclipse.jgit.treewalk.TreeWalk;
import org.eclipse.jgit.treewalk.filter.PathFilter;

import com.github.javaparser.JavaParser;
import com.github.javaparser.Position;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.body.Parameter;
import com.github.javaparser.ast.visitor.VoidVisitor;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import com.google.common.io.CharStreams;

import com.google.inject.Inject;

import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.RequirementMappingProvider;

import static java.nio.file.FileVisitResult.*;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class RepoFileVisitor extends SimpleFileVisitor<Path> implements RequirementMappingProvider {

	private Git repo;
	private final Map<String, Map<Integer, String>> blameData = new HashMap<>();
	private final Set<ReqMatcher> reqMatchers = new HashSet<>();
	private final Path sourceRootDir;

	public Collection<ReqMatcher> getReqMatcher() {
		return reqMatchers;
	}

	@Inject
	public RepoFileVisitor(ApplicationParameters vars) {
		this.sourceRootDir = new File(vars.getSourceRootDir()).toPath();
		try {

			Path tempFolder = Files.createTempDirectory("nhe-agent");
			repo = Git.cloneRepository().setURI(vars.getRepoAddress()).setDirectory(tempFolder.toFile()).call();
			Files.walkFileTree(tempFolder, this);
			reqMatchers.addAll(this.getReqMatcher());

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

		try {
			blameUnsafeVisit(file, attr);
			parseJavaFile(file);

		} catch (RevisionSyntaxException | GitAPIException | IOException e) {

			e.printStackTrace();
			return FileVisitResult.TERMINATE;
		}

		return CONTINUE;
	}

	private void parseJavaFile(Path file) throws FileNotFoundException {
		JavaParser parser = new JavaParser();

		Optional<CompilationUnit> cu = parser.parse(file.toFile()).getResult();

		if (cu.isPresent()) {

			VoidVisitor<Void> visitor = new VoidVisitorAdapter<Void>() {

				String className = "";

				@Override
				public void visit(PackageDeclaration pakage, Void arg) {
					super.visit(pakage, arg);
					className += pakage.getNameAsString();
				}

				@Override
				public void visit(ClassOrInterfaceDeclaration cd, Void arg) {
					// super.visit(cd, arg);
					className += "." + cd.getNameAsString();
					super.visit(cd, arg);
				}

				@Override
				public void visit(MethodDeclaration md, Void arg) {
					super.visit(md, arg);
					Optional<Position> pos;
					if ((pos = md.getBegin()).isPresent()) {
						if (blameData.containsKey(this.className)) {

							Collection<String> args = md.getParameters().stream()
									.map((Parameter p) -> p.getTypeAsString()).collect(Collectors.toList());
							reqMatchers.add(new ReqMatcher(this.className, md.getNameAsString(), args, pos.get().line,
									blameData.get(this.className).get(pos.get().line)));
						}

					}

				}
			};

			cu.get().accept(visitor, null);
		}
	}

	private static int countLinesOfFileInCommit(Repository repository, ObjectId commitID, String name)
			throws MissingObjectException, IncorrectObjectTypeException, IOException {
		try (RevWalk revWalk = new RevWalk(repository)) {
			RevCommit commit = revWalk.parseCommit(commitID);
			RevTree tree = commit.getTree();
			System.out.println("Having tree: " + tree);

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

	private void blameUnsafeVisit(Path file, BasicFileAttributes attr) throws GitAPIException, RevisionSyntaxException,
			AmbiguousObjectException, IncorrectObjectTypeException, IOException {

		File relativeFilePath = this.repo.getRepository().getDirectory().getParentFile().toPath().relativize(file)
				.toFile();
		if (attr.isRegularFile()
				&& com.google.common.io.Files.getFileExtension(relativeFilePath.toString()).equals("java")) {

			BlameCommand blamer = new BlameCommand(this.repo.getRepository());
			ObjectId commitID = this.repo.getRepository().resolve("HEAD");
			blamer.setStartCommit(commitID);

			blamer.setFilePath(relativeFilePath.getPath());
			BlameResult blamed = blamer.call();

			int lines = countLinesOfFileInCommit(this.repo.getRepository(), commitID, relativeFilePath.toString());
			final Pattern p = Pattern.compile(".*(#[0-9]+)");
			Map<Integer, String> fileBlameData = new HashMap<>();
			for (int i = 0; i < lines; i++) {
				RevCommit commit = blamed.getSourceCommit(i);
				Matcher m = p.matcher(commit.getShortMessage());
				if (m.matches()) {
					fileBlameData.put(i, m.group(1));
				}

			}

			String inferedClassName = this.sourceRootDir.relativize(relativeFilePath.toPath()).toString()
					.replaceAll("/", ".").replaceFirst("[.][^.]+$", "");
			this.blameData.put(inferedClassName, fileBlameData);

		}

	}

}
