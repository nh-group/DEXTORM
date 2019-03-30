package fr.pantheonsorbonne.cri.configuration.variables;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.lib.Repository;

import com.google.inject.Inject;
import com.google.inject.Singleton;

import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.mapping.GitHubRequirementIssueDecorator;

public class DemoApplicationParameters implements ApplicationParameters {

	public String getInstrumentedPackage() {
		return "fr.pantheonsorbonne.ufr27";
	}

	public final String getRepoAddress() {
		return "file:///home/nherbaut/tmp/dex/basic-cli-uni-bare";
	}

	public final String getGRPEndpointHost() {
		return "localhost";
	}

	public final Integer getGRPCEndpointPort() {
		return 8081;
	}

	@Override
	public String getSourceRootDir() {
		return SourceRootDIR.MAVEN.getPath();
	}

	@Override
	public RequirementIssueDecorator getRequirementIssueDecorator() {
		return new GitHubRequirementIssueDecorator("https://github.com/nh-group/basic-cli-uni/issues");
	}

	@Override
	public String getDiffAlgorithm() {
		return DiffAlgorithm.BLAME.toString();
	}

}
