package fr.pantheonsorbonne.cri.instrumentation.impl.jacoco;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Class;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Method;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Package;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Report;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.reqmapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.StackTraceParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.util.ArrayList;
import java.util.Collection;

public class JacocoInstrumentationClient implements InstrumentationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacocoInstrumentationClient.class);
    @Inject
    public RequirementPublisher publisher;
    @Inject
    @Named("jacocoReport")
    String jacocoReport;
    @Inject
    RequirementMappingProvider mapper;
    @Inject
    @Named("instrumentedPackage")
    String instrumentedPackage;

    @Override
    public void registerClient() {
        LOGGER.info("analysing {}", jacocoReport);

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

