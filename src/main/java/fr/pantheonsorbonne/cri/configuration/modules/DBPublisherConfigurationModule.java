package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.inject.AbstractModule;
import com.google.inject.name.Names;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.publisher.console.ConsoleRequirementsPublisher;
import fr.pantheonsorbonne.cri.publisher.db.DBRequirementPublisher;

public class DBPublisherConfigurationModule extends AbstractModule {
    String jdbc_path;

    public DBPublisherConfigurationModule(String jdbc_path) {
        this.jdbc_path = jdbc_path;
    }

    @Override
    protected void configure() {
        super.configure();

        this.bind(RequirementPublisher.class).to(DBRequirementPublisher.class);


    }
}
