package fr.pantheonsorbonne.cri.app;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

class CommitFileMaterialization {

    public Path file;
    public String issueId;
    public String commitId;

    public CommitFileMaterialization(Path file, String issueId,String commitId) {
        this.file = file;
        this.issueId = issueId;
        this.commitId=commitId;
    }

    public static Path meterialize(String fileContent) throws IOException {
        Path f = Files.createTempFile("", ".java");
        FileWriter fw = new FileWriter(f.toFile());
        fw.write(fileContent.toCharArray());
        fw.close();
        return f;

    }
}
