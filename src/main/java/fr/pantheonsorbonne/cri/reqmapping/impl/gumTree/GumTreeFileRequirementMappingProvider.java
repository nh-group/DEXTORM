package fr.pantheonsorbonne.cri.reqmapping.impl.gumTree;

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

import com.google.inject.Inject;

import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher;
import fr.pantheonsorbonne.cri.reqmapping.impl.FileRequirementMappingProvider;

public class GumTreeFileRequirementMappingProvider implements FileRequirementMappingProvider {

	@Inject
	private Repository repo;

	@Inject
	private Git git;

	private GumTreeFacade facade = new GumTreeFacade();

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

			for (CommitIssueMapping mapping : commitIssueMappings) {
				Path path = materializeFileFromCommit(this.repo, mapping.id, relativeFilePath.toString());

				diffs.add(new Diff(null, path, mapping.issueId.stream().collect(Collectors.joining(" "))));
			}

			for (int i = 1; i < diffs.size() - 1; i++) {
				diffs.get(i).src = diffs.get(i - 1).dst;
			}

			return diffs;

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

	@Override
	public Collection<ReqMatcher> getReqMatcher(Path p) {

		try {
			List<Diff> diffs = this.materializeCommitDiff(p);
			return this.facade.getReqMatcher(diffs);
		} catch (IOException | GitAPIException e) {
			e.printStackTrace();
			System.exit(-3);
			throw new RuntimeException("unreachable");
		}

	}

}
