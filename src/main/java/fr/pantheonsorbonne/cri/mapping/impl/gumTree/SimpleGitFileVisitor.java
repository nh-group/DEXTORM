package fr.pantheonsorbonne.cri.mapping.impl.gumTree;

import java.nio.CharBuffer;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
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
import java.util.stream.StreamSupport;

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
import org.eclipse.jgit.lib.ObjectReader;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

public class SimpleGitFileVisitor extends SimpleFileVisitor<Path> {

	private Git repo;

	private final Path sourceRootDir;

	@Inject
	public SimpleGitFileVisitor(ApplicationParameters vars) {
		this.sourceRootDir = new File(vars.getSourceRootDir()).toPath();
		try {

			Path tempFolder = Files.createTempDirectory("nhe-agent");
			repo = Git.cloneRepository().setURI(vars.getRepoAddress()).setDirectory(tempFolder.toFile()).call();
			Files.walkFileTree(tempFolder, this);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

		try {
			Diff p = oneStepDiff(file, attr);
			GumTreeFacade facade = new GumTreeFacade();
			facade.labelDestWithCommit(p.toDiffTree());

		} catch (RevisionSyntaxException | GitAPIException | IOException e) {

			e.printStackTrace();
			return FileVisitResult.TERMINATE;
		}

		return CONTINUE;
	}

	private Diff oneStepDiff(Path file, BasicFileAttributes attr) throws GitAPIException, RevisionSyntaxException,
			AmbiguousObjectException, IncorrectObjectTypeException, IOException {
		
		
		String treeName = "refs/heads/master"; // tag or branch
		StreamSupport.stream(this.repo.log().add(this.repo.getRepository().resolve("refs/heads/master")).call().spliterator(),false);
		

		File relativeFilePath = this.repo.getRepository().getDirectory().getParentFile().toPath().relativize(file)
				.toFile();
		if (attr.isRegularFile()
				&& com.google.common.io.Files.getFileExtension(relativeFilePath.toString()).equals("java")) {

			ObjectId commitID = this.repo.getRepository().resolve("HEAD");
			ObjectId ex = this.repo.getRepository().resolve("HEAD~1");

			Path f1 = meterializeFileFromCommit(this.repo.getRepository(), commitID, relativeFilePath.toString());
			Path f2 = meterializeFileFromCommit(this.repo.getRepository(), ex, relativeFilePath.toString());

			return new Diff(f1, f2, commitID.getName().substring(0, 7));

		}

		throw new NoSuchFileException(file.toString());

	}

	private Path meterializeFileFromCommit(Repository repository, ObjectId commitID, String name)
			throws MissingObjectException, IncorrectObjectTypeException, IOException {

		Path temp1 = Files.createTempFile(
				com.google.common.io.Files.getNameWithoutExtension(name) + "-" + commitID.getName(), ".java");
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

				FileOutputStream fos = new FileOutputStream(temp1.toFile());
				loader.copyTo(fos);

				revWalk.dispose();

			}
		}

		return temp1;
	}

}
