package fr.pantheonsorbonne.cri.mapping;

import java.net.URI;
import java.util.logging.Logger;

import org.slf4j.LoggerFactory;

import com.google.inject.Inject;

import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.requirements.Requirement;

public class GitHubRequirementIssueDecorator implements RequirementIssueDecorator {

	private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(GitHubRequirementIssueDecorator.class);
	String baseGithubURI;

	public GitHubRequirementIssueDecorator(String baseGithubURI) {
		this.baseGithubURI = baseGithubURI;
	}

	@Override
	public Requirement getIssueLink(Requirement req) {
		try {
			Integer issue = Integer.parseInt(req.getId());
			return Requirement.newBuilder(req).setIssueURI(URI.create(baseGithubURI + "/" + issue).toString()).build();
		} catch (java.lang.NumberFormatException e) {
			LOG.warn("failed to find github issue for req" + req);
			return req;
		}

	}

	@Override
	public String getRoot() {
		return this.baseGithubURI;
	}

}
