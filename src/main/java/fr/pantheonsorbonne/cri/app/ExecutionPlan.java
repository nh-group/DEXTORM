package fr.pantheonsorbonne.cri.app;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class ExecutionPlan {

    @Param({"gumtree", "blame"})
    public String diffAlgo;

    @Param({"shenyu", "dextorm-dummy-project", "dnsjava"})
    //@Param({"dextorm-dummy-project"})
    public String project;

    @Param({"instructions", "methods"})
    public String scope;

    public DextormBench bench;

    @Setup(Level.Invocation)
    public void setUp() {
        bench = new DextormBench();
    }
}
