package fr.pantheonsorbonne.cri.publisher.console;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.ConsoleAppender;
import ch.qos.logback.core.FileAppender;
import ch.qos.logback.core.OutputStreamAppender;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Collection;

public class ConsoleRequirementsPublisher implements RequirementPublisher {

    private final Logger logger;
    StringBuilder sb = new StringBuilder();

    @Inject
    public ConsoleRequirementsPublisher(@Named("consolePublishers") String consoleLoggerFileName) {


        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%date %level [%thread] %logger{10} %msg%n");
        ple.setContext(lc);
        ple.start();
        OutputStreamAppender<ILoggingEvent> appender;

        if (consoleLoggerFileName.equals("stdout")) {
            appender = new ConsoleAppender<>();
        } else {
            FileAppender fileApp = new FileAppender<>();
            fileApp.setFile(consoleLoggerFileName);
            appender = fileApp;
        }
        appender.setEncoder(ple);
        appender.setContext(lc);
        appender.start();

        logger = (Logger) LoggerFactory.getLogger(ConsoleRequirementsPublisher.class);
        logger.addAppender(appender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);


    }

    @Override
    public void publishNow(Requirement req) {
        logger.info(req.getId());


    }

    @Override
    public void publishNow(Collection<Requirement> reqToPublish) {
        for (Requirement r : reqToPublish) {
            this.publishNow(r);
        }

    }

    @Override
    public void publishNow(String project, String issue, String method, COVERAGE_TYPE coverageType, double coverage, int count) {
        logger.info("project={}\tissue={}\tmethod={}\tct={}\tcov={}\tcount={}", project, issue, method, coverageType, coverage, count);
    }

    @Override
    public void collect(String project, String issue, String method, COVERAGE_TYPE coverageType, double coverage, int count) {
        logger.info("project={}\tissue={}\tmethod={}\tct={}\tcov={}\tcount={}", project, issue, method, coverageType, coverage, count);
    }


    public void flush() {
        logger.info(sb.toString());
    }

}
