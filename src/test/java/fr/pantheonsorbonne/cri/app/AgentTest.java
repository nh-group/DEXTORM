package fr.pantheonsorbonne.cri.app;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;


class AgentTest {


    @Test
    public void testConfigParamImporter() throws IOException {
        Path tempPath = Files.createTempFile("dextorm", "yaml");
        Optional<String> res = Agent.getConfigFileFromParams(Agent.CONFIGURATION_FILE_KEY + ":" + tempPath.toAbsolutePath().toString());
        assertTrue(res.isPresent());
        assertEquals(tempPath.toString(), res.get());

        Path newTempPath = Paths.get(System.getProperty("user.dir"), tempPath.getFileName().toString());
        Files.copy(tempPath,newTempPath);

        res = Agent.getConfigFileFromParams(Agent.CONFIGURATION_FILE_KEY + ":" + newTempPath.getFileName().toString());
        assertTrue(res.isPresent());
        assertEquals(newTempPath.toAbsolutePath(), Paths.get(res.get()).toAbsolutePath());
        assertTrue(newTempPath.toFile().delete());

        assertTrue(tempPath.toFile().delete());
        res = Agent.getConfigFileFromParams(Agent.CONFIGURATION_FILE_KEY + ":" + tempPath.toAbsolutePath().toString());
        assertFalse(res.isPresent());

    }

}