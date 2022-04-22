package fr.pantheonsorbonne.cri.configuration;

import fr.pantheonsorbonne.cri.model.requirements.Requirement;

public interface RequirementIssueDecorator {

    Requirement getIssueLink(Requirement req);

    String getRoot();

}