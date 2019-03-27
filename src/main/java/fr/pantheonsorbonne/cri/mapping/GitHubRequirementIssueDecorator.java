package fr.pantheonsorbonne.cri.mapping;

import java.net.URI;

import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.requirements.Requirement;

public class GitHubRequirementIssueDecorator implements RequirementIssueDecorator {

	String baseGithubURI;

	public GitHubRequirementIssueDecorator(String baseGithubURI) {
		this.baseGithubURI = baseGithubURI;
	}

	@Override
	public Requirement getIssueLink(Requirement req) {
		String[] parsedIssue = req.getId().split("#");
		Integer issue = Integer.parseInt(parsedIssue[parsedIssue.length - 1]);
		return Requirement.newBuilder(req).setIssueURI(URI.create(baseGithubURI + "/" + issue).toString()).build();

	}

}
