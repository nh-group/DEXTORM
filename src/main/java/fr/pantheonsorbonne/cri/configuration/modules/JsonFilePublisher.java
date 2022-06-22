package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.gson.Gson;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;

public class JsonFilePublisher implements RequirementPublisher {

    @Inject
    @Named("targetDir")
    String targetDir;

    @Override
    public void publishNow(Requirement req) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void publishNow(Collection<Requirement> reqToPublish) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void publishNow(String project, String issue, String method, double lineCoverage, double methodCoverage, int countLine, int countMethod) {
        var payload = Map.of(issue, Map.of(method, Map.of("lineCoverage", lineCoverage * 100, "methodCoverage", methodCoverage * 100)));
        try (var writer = new FileWriter(Files.createTempFile(Path.of(targetDir), "dextorm", ".json").toAbsolutePath().toString())) {
            new Gson().toJson(payload, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void collect(String project, String issue, String method, double lineCoverage, double methodCoverage, int countLine, int countMethod) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void flush() {
        throw new UnsupportedOperationException();
    }
}
