package fr.pantheonsorbonne.cri.reqmapping.impl;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.RequirementMappingProvider;
import org.eclipse.jgit.errors.RevisionSyntaxException;
import org.eclipse.jgit.lib.Repository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static java.nio.file.FileVisitResult.CONTINUE;

public class GitRepoRequirementMappingProvider extends SimpleFileVisitor<Path> implements RequirementMappingProvider {

    private final static Logger LOGGER = LoggerFactory.getLogger(GitRepoRequirementMappingProvider.class);
    private final static ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(50);
    private final Set<ReqMatch> repoReqMatcherImpls = ConcurrentHashMap.newKeySet();
    private final String sourceRootDir;
    protected Repository repo;
    protected FileRequirementMappingProvider fileReqProvider;

    @Inject
    public GitRepoRequirementMappingProvider(@Named("temp-git-repo") Path tempFolderGitRepo, Repository repo,
                                             FileRequirementMappingProvider fileReqProvider, @Named("sourceRootDir")
                                                     String sourceRootDir) {


        this.sourceRootDir = Path.of(tempFolderGitRepo.toString(), sourceRootDir).toString();
        this.fileReqProvider = fileReqProvider;
        this.repo = repo;
        try {
            Files.walkFileTree(tempFolderGitRepo, this);
            EXECUTOR_SERVICE.shutdown();
            EXECUTOR_SERVICE.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);


        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    public Set<ReqMatch> getReqMatcher() {
        return repoReqMatcherImpls;
    }

    @Override
    public int countReqMatchers() {
        return this.repoReqMatcherImpls.size();
    }

    @Override
    public FileVisitResult visitFile(Path file, BasicFileAttributes attr) {

        try {

            if (file.toString().startsWith(sourceRootDir) && com.google.common.io.Files.getFileExtension(file.toString()).equals("java")) {
                LOGGER.debug("analyzing from {}", file);
                //thread
                EXECUTOR_SERVICE.submit(() -> {
                    System.out.println("[STAR] computing req from " + file);
                    var res = fileReqProvider.getReqMatcher(file);
                    System.out.println("[DONE] computing req from " + file);
                    this.repoReqMatcherImpls.addAll(res);
                    System.out.println("[DONE] there're " + this.repoReqMatcherImpls.size() + " so far");

                });
            }

        } catch (RevisionSyntaxException e) {

            e.printStackTrace();
            return FileVisitResult.TERMINATE;
        }

        return CONTINUE;
    }

}
