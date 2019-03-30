package fr.pantheonsorbonne.cri.mapping.impl.diff;

import static java.nio.file.FileVisitResult.CONTINUE;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;

import com.google.inject.Inject;
import com.google.inject.name.Named;

import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;
import fr.pantheonsorbonne.cri.mapping.RepoRequirementMappingProvider;
import fr.pantheonsorbonne.cri.mapping.ReqMatcher;
import fr.pantheonsorbonne.cri.mapping.impl.FileRequirementMappingProvider;

public class GitBlameRequirementProvider extends SimpleFileVisitor<Path> implements RepoRequirementMappingProvider {

	private final Set<ReqMatcher> repoReqMatchers = new HashSet<>();

	@Inject
	ApplicationParameters vars;

	protected Repository repo;
	FileRequirementMappingProvider fileReqProvider;

	public Collection<ReqMatcher> getReqMatcher() {
		return repoReqMatchers;
	}

	@Inject
	public GitBlameRequirementProvider(@Named("temp-git-repo") Path tempFolderGitRepo, Repository repo,
			FileRequirementMappingProvider fileReqProvider) {
		// this.sourceRootDir = new File(vars.getSourceRootDir()).toPath();
		try {
			this.fileReqProvider = fileReqProvider;
			this.repo = repo;
			Files.walkFileTree(tempFolderGitRepo, this);

		} catch (Exception e) {
			e.printStackTrace();
			System.exit(-1);
		}
	}

	@Override
	public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

		try {

			this.repoReqMatchers.addAll(fileReqProvider.getReqMatcher(file));

		} catch (RevisionSyntaxException e) {

			e.printStackTrace();
			return FileVisitResult.TERMINATE;
		}

		return CONTINUE;
	}

}
