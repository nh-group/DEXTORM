package fr.pantheonsorbonne.cri.configuration.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;
import fr.pantheonsorbonne.cri.configuration.model.collectors.GitHubIssueCollectorConfig;
import fr.pantheonsorbonne.cri.configuration.model.collectors.IssueCollectorsConfig;
import fr.pantheonsorbonne.cri.configuration.model.differ.DifferAlgorithmConfig;
import fr.pantheonsorbonne.cri.configuration.model.publisher.DBPublisherConfig;
import fr.pantheonsorbonne.cri.configuration.model.publisher.GrpcPublisherConfig;
import fr.pantheonsorbonne.cri.configuration.model.publisher.PublisherConfig;
import fr.pantheonsorbonne.cri.configuration.model.publisher.RESTPublisherConfig;
import fr.pantheonsorbonne.cri.configuration.modules.*;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class GeneralConfiguration {
    public AppConfiguration app;
    public IssueCollectorsConfig issueCollectors;
    public PublisherConfig publishers;
    public Map<String, DifferAlgorithmConfig> differs;
    @JsonIgnore
    public Collection<Module> inheritedModules;

    public GeneralConfiguration() {
    }

    public Map<String, DifferAlgorithmConfig> getDiffers() {
        return differs;
    }

    public void setDiffers(Map<String, DifferAlgorithmConfig> differs) {
        this.differs = differs;
    }

    public void setInheritedModules(Collection<Module> inheritedModules) {
        this.inheritedModules = inheritedModules;
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
                ", differConfig=" + differs +

                '}';
    }

    public Set<Module> getModules() {
        Set<Module> res = new HashSet<>(this.inheritedModules);


        GitRepoProviderModule gitRepoProviderModule = null;
        //how to we git?
        {

            if ((this.issueCollectors.github.containsKey(this.app.getIssueCollectorName()))) {
                GitHubIssueCollectorConfig gitHubConfig = this.issueCollectors.github.get(this.app.getIssueCollectorName());
                gitRepoProviderModule = new GitHubRepoProviderModule(gitHubConfig.getGitHubRepoName(), gitHubConfig.getRepoAddress());


            } else {
                throw new UnsupportedOperationException("only github is supported at this point");
            }

            res.add(gitRepoProviderModule);
        }

        {
            //who to we diff?
            if (this.differs != null && this.differs.containsKey(this.app.getDiffAlgorithmName())) {
                DifferAlgorithmConfig config = this.differs.get(this.app.getDiffAlgorithmName());
                res.add(new RequirementMappingConfigurationModule(config.getDiffAlgorithm(), config.methods, config.instructions, gitRepoProviderModule.getRequirementIssueDecorator()));


            }
        }


        {
            //how we publish results?
            if (this.publishers.filePublishers != null && this.publishers.filePublishers.containsKey(this.app.getPublisherName())) {
                String filePath = this.publishers.filePublishers.get(this.app.getPublisherName()).getFilePath();
                if (filePath == null || filePath.isBlank()) {
                    throw new IllegalArgumentException("you MUST specify a filePath for the console logger " + this.app.getPublisherName());
                }
                res.add(new ConsolePublisherConfigurationModule(filePath));
            } else if (this.publishers.grpcPublishers != null && this.publishers.grpcPublishers.containsKey(this.app.getPublisherName())) {
                GrpcPublisherConfig grpcPublisherConfig = this.publishers.grpcPublishers.get(this.app.getPublisherName());
                res.add(new GRPCPublisherConfigurationModule(grpcPublisherConfig.getHost(), grpcPublisherConfig.getPort()));
            } else if (this.publishers.dbPublishers != null && this.publishers.dbPublishers.containsKey(this.app.getPublisherName())) {
                DBPublisherConfig dbPublisherConfig = this.publishers.dbPublishers.get(this.app.getPublisherName());
                res.add(new DBPublisherConfigurationModule(dbPublisherConfig.getJdbc_path()));

            } else if (this.publishers.restPublishers != null && this.publishers.restPublishers.containsKey(this.app.getPublisherName())) {
                RESTPublisherConfig publisherConfig = this.publishers.restPublishers.get(this.app.getPublisherName());
                res.add(new APIPublisherConfigurationModule(publisherConfig.getBaseUrl()));
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
