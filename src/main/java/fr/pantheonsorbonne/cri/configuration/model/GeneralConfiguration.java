package fr.pantheonsorbonne.cri.configuration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import fr.pantheonsorbonne.cri.configuration.RequirementIssueDecorator;
import fr.pantheonsorbonne.cri.configuration.modules.*;
import fr.pantheonsorbonne.cri.configuration.variables.DiffAlgorithm;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

public class GeneralConfiguration {
    AppConfiguration app;
    IssueCollectorsConfig issueCollectors;
    PublisherConfig publishers;

    public void setInheritedModules(Collection<Module> inheritedModules) {
        this.inheritedModules = inheritedModules;
    }

    @JsonIgnore
    Collection<Module> inheritedModules;

    public GeneralConfiguration() {
    }


    public AppConfiguration getApp() {
        return app;
    }

    public void setApp(AppConfiguration app) {
        this.app = app;
    }

    public IssueCollectorsConfig getIssueCollectors() {
        return issueCollectors;
    }

    public void setIssueCollectors(IssueCollectorsConfig issueCollectors) {
        this.issueCollectors = issueCollectors;
    }

    public PublisherConfig getPublishers() {
        return publishers;
    }

    public void setPublishers(PublisherConfig publishers) {
        this.publishers = publishers;
    }

    @Override
    public String toString() {
        return "GeneralConfiguration{" +
                "app=" + app +
                ", issueCollectors=" + issueCollectors +
                ", publishers=" + publishers +
                '}';
    }

    public Set<Module> getModules() {
        Set<Module> res = new HashSet<>(this.inheritedModules);



        GitRepoProviderModule gitRepoProviderModule = null;
        //how to we git?
        {

            if ((this.issueCollectors.github.containsKey(this.app.getIssueCollectorName()))) {
                gitRepoProviderModule = new GitHubRepoProviderModule("https://github.com/" + this.issueCollectors.github.get(this.app.getIssueCollectorName()).getRepo());

            } else {
                throw new UnsupportedOperationException("only github is supported at this point");
            }

            res.add(gitRepoProviderModule);
        }

        {
            //who to we diff?
            res.add(new RequirementMappingConfigurationModule(DiffAlgorithm.valueOf(this.app.getDiffAlgorithmName()), gitRepoProviderModule.getRequirementIssueDecorator()));
        }

        {
            //how we publish results?
            if (this.publishers.consolePublishers.containsKey(this.app.getPublisherName())) {
                res.add(new ConsolePublisherConfigurationModule());
            } else if (this.publishers.grpcPublishers.containsKey(this.app.getPublisherName())) {
                GrpcPublisherConfig grpcPublisherConfig = this.publishers.grpcPublishers.get(this.app.getPublisherName());
                res.add(new GRPCPublisherConfigurationModule(grpcPublisherConfig.getHost(), grpcPublisherConfig.getPort()));
            }

        }

        res.add(new AbstractModule() {
            @Override
            protected void configure() {
                super.configure();

                for (Field field : app.getClass().getDeclaredFields()) {

                    try {
                        Class klass = field.getType();
                        this.bind(klass).annotatedWith(Names.named(field.getName())).toInstance(klass.cast(field.get(app)));
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }


            }
        });

        return res;


    }
}
