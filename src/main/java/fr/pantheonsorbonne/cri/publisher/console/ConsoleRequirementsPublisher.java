package fr.pantheonsorbonne.cri.publisher.console;

import java.util.Collection;


import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.encoder.PatternLayoutEncoder;
import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.FileAppender;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import org.slf4j.LoggerFactory;
import org.slf4j.event.EventRecodingLogger;

import javax.inject.Inject;
import javax.inject.Named;

public class ConsoleRequirementsPublisher implements RequirementPublisher {

    @Inject
    public ConsoleRequirementsPublisher(@Named("consolePublishers") String consoleLoggerFileName) {

        LoggerContext lc = (LoggerContext) LoggerFactory.getILoggerFactory();
        PatternLayoutEncoder ple = new PatternLayoutEncoder();
        ple.setPattern("%date %level [%thread] %logger{10} [%file:%line] %msg%n");
        ple.setContext(lc);
        ple.start();
        FileAppender<ILoggingEvent> fileAppender = new FileAppender<>();
        fileAppender.setFile(consoleLoggerFileName);
        fileAppender.setEncoder(ple);
        fileAppender.setContext(lc);
        fileAppender.start();

        logger = (Logger) LoggerFactory.getLogger(ConsoleRequirementsPublisher.class);
        logger.addAppender(fileAppender);
        logger.setLevel(Level.INFO);
        logger.setAdditive(false);


    }

    private final Logger logger;

    @Override
    public void publish(Requirement req) {
        logger.info(req.getId());


    }

    @Override
    public void publish(Collection<Requirement> reqToPublish) {
        for (Requirement r : reqToPublish) {
            this.publish(r);
        }

    }

}
