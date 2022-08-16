package fr.pantheonsorbonne.cri.instrumentation.impl.jacoco;

import com.google.inject.Inject;
import com.google.inject.name.Named;
import fr.pantheonsorbonne.cri.instrumentation.InstrumentationClient;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Class;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.Package;
import fr.pantheonsorbonne.cri.instrumentation.impl.jacoco.model.*;
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
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class JacocoInstrumentationClient implements InstrumentationClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(JacocoInstrumentationClient.class);
    @Inject
    public RequirementPublisher publisher;
    @Inject
    @Named("coverageFolder")
    String coverageFolder;
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
    @Inject
    @Named("gitHubRepoName")
    String gitHubRepoName;

    @Inject
    @Named("diffMethod")
    String diffMethod;

    @Override
    public void registerClient() {
        LOGGER.info("analysing {}", coverageFolder);


        LOGGER.debug("get jacoco data");
        List<Report> reports = getReportObjectFromXMl();
        List<StackTraceElement> stackTracesCovered = new ArrayList<>();
        List<StackTraceElement> stackTracesAll = new ArrayList<>();
        for (Report report : reports) {
            LOGGER.debug("getting the covered elements");
            stackTracesCovered.addAll(extractStackTraceElement(report, this.doMethods, true, this.doInstructions, true));

            LOGGER.debug("getting all the elements");
            stackTracesAll.addAll(extractStackTraceElement(report, this.doMethods, false, this.doInstructions, false));
        }
        LOGGER.debug("get the matcher from the code<->req mapper");
        Set<ReqMatch> requirementsMatchers = mapper.getReqMatcher();
        Set<String> reqIds = requirementsMatchers.stream().map(rm -> rm.getRequirementsIds()).flatMap(Collection::stream).collect(Collectors.toSet());


        LOGGER.debug("Matches elements with the mapper");
        ElementMapper parserCovered = new ElementMapper(stackTracesCovered.toArray(new StackTraceElement[0]), instrumentedPackage, requirementsMatchers);
        ElementMapper parserAll = new ElementMapper(stackTracesAll.toArray(new StackTraceElement[0]), instrumentedPackage, requirementsMatchers);


        LOGGER.debug("//only covered");
        var covered = parserCovered.getMatchedElements();
        //every element
        LOGGER.debug("//every element");
        var all = parserAll.getMatchedElements();

        List<ReqMatch> rms = new ArrayList<>(requirementsMatchers).stream().collect(Collectors.toList());
        //Collections.sort(rms);
        for (ReqMatch rm : rms) {
            LOGGER.debug("//every element");
            StackTraceElement matchee = null;
            for (StackTraceElement ste : all) {
                if (rm.isMatch(ste)) {

                    matchee = ste;
                    break;
                }
            }
            if (matchee != null) {
                if (covered.contains(matchee)) {
                    System.out.println("COVERED\t" + rm + " by " + matchee);
                } else {
                    System.out.println("NOCOVRD\t" + rm + " by " + matchee);
                }

            } else {
                System.out.println("IRRLVNT\t" + rm.toString());
            }
        }
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
        for (String reqId : reqIds) {
            coverageInfo.put(reqId, Double.valueOf(allReqIdList.stream().filter(req -> req.equals(reqId)).count()));
        }

        //update the map to get the ratio between covered and all
        var coveredReqIdList = parserCovered.getMatchingRequirementsIdList();
        for (String reqId : reqIds) {
            Double countTotal = coverageInfo.get(reqId);
            Long countCovered = coveredReqIdList.stream().filter(req -> req.equals(reqId)).count();
            Double ratio = countCovered / countTotal;
            if (Double.isNaN(ratio)) {
                continue;
            }
            coverageInfo.put(reqId, ratio);
            if (this.doMethods) {
                publisher.publishNow(gitHubRepoName, reqId, diffMethod, RequirementPublisher.COVERAGE_TYPE.METHODS, ratio, countCovered.intValue());
            } else {
                publisher.publishNow(gitHubRepoName, reqId, diffMethod, RequirementPublisher.COVERAGE_TYPE.LINES, ratio, countCovered.intValue());
            }

            //System.out.println("Coverage " + reqId + " : " + 100 * ratio + " (" + countCovered + "/" + countTotal + ")");
        }



    /*
        parserCovered.getMatchingRequirementsIdSet()
                .stream()
                .map((String req) -> Requirement.newBuilder().setId(req).build())
                .forEach((Requirement req) -> publisher.publish(req));
*/

    }

    private List<Report> getReportObjectFromXMl() {
        List<Report> res = new ArrayList<>();
        try {
            List<String> reports = Files.walk(Path.of(this.coverageFolder)).filter(Files::isRegularFile).map(p -> p.toString()).collect(Collectors.toList());

            for (String report : reports) {
                try {
                    LOGGER.info("adding {} coverage report", report);
                    JAXBContext jc = JAXBContext.newInstance(Report.class);
                    XMLInputFactory xif = XMLInputFactory.newFactory();
                    xif.setProperty(XMLInputFactory.SUPPORT_DTD, false);
                    XMLStreamReader xsr = xif.createXMLStreamReader(new StreamSource(report));
                    Unmarshaller unmarshaller = jc.createUnmarshaller();
                    res.add((Report) unmarshaller.unmarshal(xsr));


                } catch (JAXBException e) {
                    e.printStackTrace();
                } catch (XMLStreamException e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return res;
    }

    private static List<StackTraceElement> extractStackTraceElement(Report report, boolean doMethods, boolean onlyCoveredMethods, boolean doInstructions, boolean onlyCoveredInstructions) {
        Predicate<Counter> methodPredicate;
        if (onlyCoveredMethods) {
            methodPredicate = c -> {
                return c.getCovered().equals("1");
            };
        } else {
            methodPredicate = c -> true;
        }

        Predicate<Line> linePredicate;
        if (onlyCoveredInstructions) {
            linePredicate = line -> line.getCb() > 0 || line.getCi() > 0;
        } else {
            linePredicate = l -> true;
        }

        ExecutorService executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
        List<StackTraceElement> stackTraces = Collections.synchronizedList(new ArrayList<>());
        AtomicInteger atomicInteger = new AtomicInteger();
        for (Package pakage : report.getPackages()) {
            for (Class klass : pakage.getClazz()) {
                if (doMethods) {
                    executorService.submit(() -> {
                        atomicInteger.incrementAndGet();
                        extractMethodInfo(methodPredicate, stackTraces, pakage, klass);
                        atomicInteger.decrementAndGet();
                    });
                }
                if (doInstructions) {
                    executorService.submit(() -> {
                        atomicInteger.incrementAndGet();
                        extractInstructionInfo(linePredicate, stackTraces, pakage, klass);
                        atomicInteger.decrementAndGet();
                    });
                }
            }


        }


        int value;
        while ((value = atomicInteger.get()) > 0) {
            try {
                Thread.sleep(2l);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            //System.out.println("NHE remaining tasks " + atomicInteger.get());
        }


        executorService.shutdownNow();
        return stackTraces;
    }

    private static void extractMethodInfo(Predicate<Counter> methodPredicate, List<StackTraceElement> stackTraces, Package pakage, Class klass) {
        for (Method method : klass.getMethod()) {
            String[] klassBits = klass.getName().replaceAll("/", ".").split("\\.");
            String klassName = klassBits[klassBits.length - 1];
            if (method.getCounter().stream().anyMatch(methodPredicate)) {
                stackTraces.add(new StackTraceElement(klass.getSourcefilename(),
                        pakage.getName().replaceAll("/", "."), klassName, method.getName(), method.getDesc(), method.getLine()));
            }
        }
    }

    private static void extractInstructionInfo(Predicate<Line> linePredicate, List<StackTraceElement> stackTraces, Package pakage, Class klass) {
        Sourcefile sourcefile = pakage.getSourceFile().stream().filter(sf -> sf.getName().equals(klass.getSourcefilename())).findAny().orElseThrow();
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


                stackTraces.add(
                        new StackTraceElement(
                                klass.getSourcefilename(),
                                pakage.getName().replaceAll("/", "."),
                                Arrays.stream(klass.getName().split("/")).reduce((f, l) -> l).get(),
                                "",
                                "()V",
                                line.getNr()));
            }
        }
    }
}

