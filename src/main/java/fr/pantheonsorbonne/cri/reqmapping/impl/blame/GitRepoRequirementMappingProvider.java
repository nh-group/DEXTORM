package fr.pantheonsorbonne.cri.reqmapping.impl.blame;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher;
import fr.pantheonsorbonne.cri.reqmapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.impl.FileRequirementMappingProvider;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

import static java.nio.file.FileVisitResult.CONTINUE;

public class GitRepoRequirementMappingProvider extends SimpleFileVisitor<Path> implements RequirementMappingProvider {


    private final Set<ReqMatcher> repoReqMatchers = new HashSet<>();
    protected Repository repo;
    protected FileRequirementMappingProvider fileReqProvider;

    @Inject
    public GitRepoRequirementMappingProvider(@Named("temp-git-repo") Path tempFolderGitRepo, Repository repo,
                                             FileRequirementMappingProvider fileReqProvider) {

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
    public Collection<ReqMatcher> getReqMatcher() {
        return repoReqMatchers;
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

        try {

            if (com.google.common.io.Files.getFileExtension(file.toString()).equals("java")) {
                this.repoReqMatchers.addAll(fileReqProvider.getReqMatcher(file));
            }

        } catch (RevisionSyntaxException e) {

            e.printStackTrace();
            return FileVisitResult.TERMINATE;
        }

        return CONTINUE;
    }

}
