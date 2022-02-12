package fr.pantheonsorbonne.cri.configuration.variables;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.mapping.GitHubRequirementIssueDecorator;

public class ConfigurationFileParameters implements ApplicationParameters {

	private Properties properties;

	protected ConfigurationFileParameters() throws FileNotFoundException, IOException {
		this.properties = new Properties();
		try (FileInputStream fis = new FileInputStream("dextorm.properties")) {
			this.properties.load(fis);
		}

	}

	@Override
	public Integer getGRPCEndpointPort() {

		return Integer.parseInt(this.properties.getProperty("GRPCEndpointPort"));
	}

	@Override
	public String getGRPEndpointHost() {
		return this.properties.getProperty("GRPCEndpointHost");
	}

	@Override
	public String getRepoAddress() {
		return this.properties.getProperty("RepoAddress");
	}

	@Override
	public String getInstrumentedPackage() {
		return this.properties.getProperty("InstrumentedPackage");
	}

	@Override
	public String getSourceRootDir() {
		return this.properties.getProperty("sourceRootDir");
	}

	@Override
	public RequirementIssueDecorator getRequirementIssueDecorator() {
		if ("github".equals(this.properties.getProperty("ReqIssueDecorator"))) {
			return new GitHubRequirementIssueDecorator(this.properties.getProperty("RemoteRepoIssues"));
		}

		throw new RuntimeException("failed to load issue provider");
	}

	@Override
	public String getDiffAlgorithm() {
		return this.properties.getProperty("diffAlgorithm", DiffAlgorithm.GUMTREE.toString());
	}

}
