package fr.pantheonsorbonne.cri.configuration.modules;

import com.google.gson.GsonBuilder;
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
        if (Double.isNaN(lineCoverage)) {
            lineCoverage = 0;
        }
        if (Double.isNaN(methodCoverage)) {
            methodCoverage = 0;
        }
        var payload = Map.of(issue, Map.of(method, Map.of("lineCoverage", lineCoverage * 100, "methodCoverage", methodCoverage * 100)));
        if (!Files.exists(Path.of(targetDir))) {
            try {
                Files.createDirectory(Path.of(targetDir));
            } catch (IOException e) {
                e.printStackTrace();
                System.exit(-1);
            }
        }
        try (var writer = new FileWriter(Files.createTempFile(Path.of(targetDir), "dextorm", ".json").toAbsolutePath().toString())) {
            new GsonBuilder().serializeSpecialFloatingPointValues().create().toJson(payload, writer);
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
