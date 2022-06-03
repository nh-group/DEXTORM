package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Level;
import java.util.logging.Logger;


public abstract class GitRepoProviderModule extends AbstractModule {

    private static final Logger logger = Logger.getLogger(GitRepoProviderModule.class.getName());
    protected String issueCollectorAddress;
    protected String repoAddress;


    public GitRepoProviderModule(String isseCollectorAddress, String repoAddress) {
        this.issueCollectorAddress = isseCollectorAddress;
        this.repoAddress = repoAddress;
    }

    @Provides
    @Singleton
    @Named("temp-git-repo")
    private Path getTempFolder() {
        try {
            return Files.createTempDirectory("dextorm");
        } catch (IOException e) {
            logger.info("failed to create temp folder " + e.getMessage());
            return null;
        }
    }

    @Inject
    @Provides
    @Singleton
    private Git getRepository(@Named("temp-git-repo") Path tempFolder) {

        try {
            logger.log(Level.INFO, "cloning repository from" + repoAddress + " to " + tempFolder);
            return Git.cloneRepository().setURI(repoAddress).setDirectory(tempFolder.toFile()).setBare(false).call();

        } catch (GitAPIException e) {

            throw new RuntimeException(e);

        }

    }

    @Inject
    @Provides
    public Repository getRepository(Git git) {

        return git.getRepository();

    }

    @Provides
    public Class<? extends RequirementIssueDecorator> getRequirementIssueDecorator() {
        return this.getInternalRequirementIssueDecorator();
    }

    protected abstract Class<? extends RequirementIssueDecorator> getInternalRequirementIssueDecorator();

}
