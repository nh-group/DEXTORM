package fr.pantheonsorbonne.cri.configuration;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.logging.Logger;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import com.github.javaparser.utils.Log;
import com.google.common.base.Throwables;
import com.google.inject.AbstractModule;
import com.google.inject.Inject;
import com.google.inject.Provides;
import com.google.inject.Singleton;
import com.google.inject.name.Named;

import fr.pantheonsorbonne.cri.configuration.variables.ApplicationParameters;

public class GitRepoProvider extends AbstractModule {

	@Inject
	Logger logger;

	@Provides
	@Singleton
	@Named("temp-git-repo")
	public Path getTempFolder() {
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
	public Repository getRepository(@Named("temp-git-repo") Path tempFolder, ApplicationParameters params) {

		try {

			Git repo = Git.cloneRepository().setURI(params.getRepoAddress()).setDirectory(tempFolder.toFile()).call();
			return repo.getRepository();
		} catch (GitAPIException e) {

			throw new RuntimeException(e);

		}

	}

}
