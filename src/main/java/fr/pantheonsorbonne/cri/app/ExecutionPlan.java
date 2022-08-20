package fr.pantheonsorbonne.cri.app;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class ExecutionPlan {

    @Param({"gumtree", "blame"})
    //@Param({"blame"})
    public String diffAlgo;

    //@Param({"shenyu", "dextorm-dummy-project", "dnsjava", "RxJava"})
    @Param({"dnsjava"})
    public String project;

    @Param({"methods", "instructions"})
    //@Param({"instructions"})
    public String scope;

    public DextormBench bench;

    @Setup(Level.Invocation)
    public void setUp() {
        bench = new DextormBench();
    }
}
