package fr.pantheonsorbonne.cri.app;

import org.openjdk.jmh.annotations.*;

@State(Scope.Benchmark)
public class ExecutionPlan {

    @Param({"gumtree", "blame"})
    public String diffAlgo;

    @Param({"basic-cli-uni", "dextorm-dummy-project"})
    public String project;

    @Param({"instructions", "methods"})
    public String scope;

    public DextormBench bench;

    public String password = "4v3rys3kur3p455w0rd";

    @Setup(Level.Invocation)
    public void setUp() {
        bench = new DextormBench();
    }
}
