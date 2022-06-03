package fr.pantheonsorbonne.cri.instrumentation.impl.jacoco;

import com.google.common.collect.Sets;
import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Class;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Package;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.*;
import fr.pantheonsorbonne.cri.model.requirements.Requirement;
import fr.pantheonsorbonne.cri.publisher.RequirementPublisher;
import fr.pantheonsorbonne.cri.reqmapping.ElementMapper;
import fr.pantheonsorbonne.cri.reqmapping.ReqMatch;
import fr.pantheonsorbonne.cri.reqmapping.RequirementMappingProvider;
import fr.pantheonsorbonne.cri.reqmapping.StackTraceElement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import javax.xml.transform.stream.StreamSource;
import java.util.*;
import java.util.function.Predicate;

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
    @Inject
    @Named("DoMethodsDiff")
    Boolean doMethods;
    @Inject
    @Named("DoInstructionsDiff")
    Boolean doInstructions;

    @Override
    public void registerClient() {
        LOGGER.info("analysing {}", jacocoReport);

        //get Jacoco Data
        Report report = getReportObjectFromXMl();
        //get the covered elements
        List<StackTraceElement> stackTracesCovered = extractStackTraceElement(report, this.doMethods, true, this.doInstructions, true);
        //get all the elements
        List<StackTraceElement> stackTracesAll = extractStackTraceElement(report, this.doMethods, false, this.doInstructions, false);

        //get the matcher from the code<->req mapper
        Set<ReqMatch> requirementsMatchers = mapper.getReqMatcher();

        //Matches elements with the mapper
        ElementMapper parserCovered = new ElementMapper(stackTracesCovered.toArray(new StackTraceElement[0]), instrumentedPackage, requirementsMatchers);
        ElementMapper parserAll = new ElementMapper(stackTracesAll.toArray(new StackTraceElement[0]), instrumentedPackage, requirementsMatchers);


        //only covered
        var covered = parserCovered.getMatchedElements();
        //every element
        var all = parserAll.getMatchedElements();
        var uncovered = new HashSet<>(all);
        uncovered.removeAll(covered);

        //mapp uncovered element
        var parseUncovered = new ElementMapper(uncovered.toArray(new StackTraceElement[0]), instrumentedPackage, requirementsMatchers);

        //for each element, the correspondings reqId
        var uncoveredReqIdList = parseUncovered.getMatchingRequirementsIdList();

        //map collecting coverage info
        Map<String, Double> coverageInfo = new HashMap<>();

        //fill the map with all the id from all element
        var allReqIdList = parserAll.getMatchingRequirementsIdList();
        for (String reqId : Sets.newHashSet(allReqIdList.iterator())) {
            coverageInfo.put(reqId, Double.valueOf(allReqIdList.stream().filter(req -> req.equals(reqId)).count()));
        }

        //update the map to get the ratio between covered and all
        var coveredReqIdList = parserCovered.getMatchingRequirementsIdList();
        for (String reqId : Sets.newTreeSet(coveredReqIdList)) {
            Double countTotal = coverageInfo.get(reqId);
            Double countCovered = Double.valueOf(coveredReqIdList.stream().filter(req -> req.equals(reqId)).count());
            Double ratio = countCovered / countTotal;
            coverageInfo.put(reqId, ratio);
            System.out.println("Coverage " + reqId + " : " + 100 * ratio + " (" + countCovered + "/" + countTotal + ")");
        }


        parserCovered.getMatchingRequirementsIdSet()
                .stream()
                .map((String req) -> Requirement.newBuilder().setId(req).build())
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

    private static List<StackTraceElement> extractStackTraceElement(Report report, boolean doMethods, boolean onlyCoveredMethods, boolean doInstructions, boolean onlyCoveredInstructions) {
        Predicate<Counter> methodPredicate;
        if (onlyCoveredMethods) {
            methodPredicate = c -> c.getCovered().equals("1");
        } else {
            methodPredicate = c -> true;
        }

        Predicate<Line> linePredicate;
        if (onlyCoveredInstructions) {
            linePredicate = line -> line.getCb() > 0 || line.getCi() > 0;
        } else {
            linePredicate = l -> true;
        }


        List<StackTraceElement> stackTraces = new ArrayList<>();
        for (Package pakage : report.getPackages()) {
            for (Class klass : pakage.getClazz()) {
                if (doMethods) {
                    for (Method method : klass.getMethod()) {
                        String[] klassBits = klass.getName().replaceAll("/", ".").split("\\.");
                        String klassName = klassBits[klassBits.length - 1];
                        if (method.getCounter().stream().anyMatch(methodPredicate)) {
                            stackTraces.add(new StackTraceElement(klass.getSourcefilename(),
                                    pakage.getName().replaceAll("/", "."), klassName, method.getName(), method.getDesc(), method.getLine()));
                        }
                    }
                }
                if (doInstructions) {
                    for (Sourcefile sourcefile : pakage.getSourceFile()) {

                        for (Line line : sourcefile.getLine()) {
                            //https://stackoverflow.com/questions/33868761/how-to-interpret-the-jacoco-xml-file
                        /*
                        mi = missed instructions (statements)
                        ci = covered instructions (statements)
                        mb = missed branches
                        cb = covered branches

                        When mb or cb is greater then 0 the line is a branch.
                        When mb and cb are 0 the line is a statement.
                        cb / (mb+cb) (line 11) is 2/4 partial hit
                        When not a branch and mi == 0 the line is hit

                        cb>0||ci>0 => hit

                         */
                            if (linePredicate.test(line)) {

                                Optional<Method> method = klass.getMethod().stream().filter(m -> m.getLine() >= line.getNr()).findFirst();
                                if (method.isPresent()) {
                                    stackTraces.add(
                                            new StackTraceElement(
                                                    klass.getSourcefilename(),
                                                    pakage.getName(),
                                                    klass.getName(),
                                                    method.get().getName(),
                                                    method.get().getDesc(),
                                                    line.getNr()));
                                }
                            }
                        }
                    }
                }
            }


        }
        return stackTraces;
    }
}

