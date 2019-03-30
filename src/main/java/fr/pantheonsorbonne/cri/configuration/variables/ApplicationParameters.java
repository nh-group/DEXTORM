package fr.pantheonsorbonne.cri.configuration.variables;

import java.io.IOException;

import org.eclipse.jgit.lib.Repository;

import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;

public interface ApplicationParameters {

	public enum DiffAlgorithm {
		GUMTREE, BLAME;
		
	}

	public enum SourceRootDIR {
		MAVEN("src/main/java");

		String str;

		SourceRootDIR(String str) {
			this.str = str;
		}

		public String getPath() {
			return this.str;
		}
	}

	public abstract Integer getGRPCEndpointPort();

	public abstract String getGRPEndpointHost();

	public abstract String getRepoAddress();

	public abstract String getInstrumentedPackage();

	public abstract String getSourceRootDir();

	public abstract RequirementIssueDecorator getRequirementIssueDecorator();

	public abstract String getDiffAlgorithm();

}
