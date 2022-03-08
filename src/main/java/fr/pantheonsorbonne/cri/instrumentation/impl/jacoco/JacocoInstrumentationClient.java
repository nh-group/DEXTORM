package fr.pantheonsorbonne.cri.instrumentation.impl.jacoco;

import com.google.inject.Inject;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import org.jacoco.core.analysis.*;
import org.jacoco.core.data.*;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class JacocoInstrumentationClient implements InstrumentationClient {


    private int getHitCount(final boolean[] data) {
        int count = 0;
        for (final boolean hit : data) {
            if (hit) {
                count++;
            }
        }
        return count;
    }

    private final static Logger LOGGER = LoggerFactory.getLogger(JacocoInstrumentationClient.class);
    @Inject
    Instrumentation instrumentation;

    @Override
    public void registerClient() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    ByteArrayOutputStream os = new ByteArrayOutputStream();
                    Socket socket = new Socket(InetAddress.getByName("localhost"), 6300);
                    ExecutionDataStore store = new ExecutionDataStore();
                    RemoteControlReader reader = new RemoteControlReader(socket.getInputStream());
                    RemoteControlWriter writer = new RemoteControlWriter(socket.getOutputStream());
                    ExecutionDataWriter localWriter = new ExecutionDataWriter(os);
                    ExecutionDataStore executionDataStore = new ExecutionDataStore();
                    reader.setExecutionDataVisitor(executionDataStore);
                    reader.setSessionInfoVisitor(localWriter);

                    new Thread(() -> {
                        try {
                            writer.visitDumpCommand(true, true);
                            while (reader.read()) {

                                Thread.sleep(10000);


                                final CoverageBuilder coverageBuilder = new CoverageBuilder();
                                final Analyzer analyzer = new Analyzer(
                                        executionDataStore, coverageBuilder);

                                analyzer.analyzeAll(new File("/home/nherbaut/workspace/dextorm/basic-cli-uni/target/classes/fr/pantheonsorbonne/ufr27/action/AddTeacherAction.class"));

                                for (IClassCoverage cc : coverageBuilder.getClasses()) {
                                    for (IMethodCoverage mc : cc.getMethods()) {
                                        for (int i = mc.getFirstLine(); i < mc.getLastLine(); i++) {
                                            ILine line = mc.getLine(i);
                                            LOGGER.info(line.getInstructionCounter().toString());
                                        }

                                    }

                                }

                                writer.visitDumpCommand(true, true);

                            }
                        } catch (Throwable t) {
                            t.printStackTrace();
                        }
                        LOGGER.warn("end reading");

                    }).start();

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();


    }
}

