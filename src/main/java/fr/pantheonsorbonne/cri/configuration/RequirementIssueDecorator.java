package fr.pantheonsorbonne.cri.configuration;

import fr.pantheonsorbonne.cri.requirements.Requirement;

public interface RequirementIssueDecorator {

	public Requirement getIssueLink(Requirement req);

	public String getRoot();

}