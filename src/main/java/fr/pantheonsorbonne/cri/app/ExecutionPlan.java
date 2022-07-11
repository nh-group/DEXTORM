package fr.pantheonsorbonne.cri.app;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class ExecutionPlan {

    @Param({"gumtree", "blame"})
    public String diffAlgo;

    @Param({"basic-cli-uni", "dextorm-dummy-project", "dnsjava", "commons-lang"})
    public String project;

    @Param({"instructions", "methods"})
    public String scope;

    public DextormBench bench;

    @Setup(Level.Invocation)
    public void setUp() {
        bench = new DextormBench();
    }
}
