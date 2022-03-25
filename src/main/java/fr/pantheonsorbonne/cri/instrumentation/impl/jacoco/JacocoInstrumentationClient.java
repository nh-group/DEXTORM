package fr.pantheonsorbonne.cri.instrumentation.impl.jacoco;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.*;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Class;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Package;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatcher;
import fr.pantheonsorbonne.cri.reqmapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.StackTraceParser;
import org.jacoco.core.analysis.*;
import org.jacoco.core.data.*;
import org.jacoco.core.instr.Instrumenter;
import org.jacoco.core.runtime.*;
import org.jacoco.report.internal.xml.ReportElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.text.html.Option;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBContextFactory;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.lang.instrument.Instrumentation;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

public class JacocoInstrumentationClient implements InstrumentationClient {

    @Inject
    @Named("jacocoReport")
    String jacocoReport;

    @Inject
    RequirementMappingProvider mapper;

    @Inject
    @Named("instrumentedPackage")
    String instrumentedPackage;

    @Inject
    public RequirementPublisher publisher;

    @Override
    public void registerClient() {
        System.out.println("analysing" + jacocoReport);

        Report report = getReportObjectFromXMl();
        Collection<StackTraceElement> stackTraces = new ArrayList<>();
        for (Package pakage : report.getPackages()) {
            for (Class klass : pakage.getClazz()) {
                for (Method method : klass.getMethod()) {
                    stackTraces.add(new StackTraceElement(klass.getName(), method.getName(), klass.getSourcefilename(), Integer.parseInt(method.getLine())));
                }
            }


        }
        StackTraceParser parser = new StackTraceParser(stackTraces.toArray(new StackTraceElement[0]), instrumentedPackage, mapper.getReqMatcher());
        Collection<String> resq = parser.getReqs();

        parser.getReqs().stream().map((String req) -> Requirement.newBuilder().setId(req).build())
                .forEach((Requirement req) -> publisher.publish(req));


    }

    private Report getReportObjectFromXMl() {
        try {
            JAXBContext jc = JAXBContext.newInstance(Report.class);
            XMLInputFactory xif = XMLInputFactory.newFactory();
            xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
            XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(jacocoReport));
            Unmarshaller unmarshaller = jc.createUnmarshaller();
            return (Report) unmarshaller.unmarshal(xsr);


        } catch (JAXBException e) {
            e.printStackTrace();
        } catch (XMLStreamException e) {
            e.printStackTrace();
        }

        System.exit(-1);
        return null;
    }
}

