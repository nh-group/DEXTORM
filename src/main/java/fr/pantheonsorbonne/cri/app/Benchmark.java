package fr.pantheonsorbonne.cri.app;

import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;

import java.io.IOException;
import java.net.URISyntaxException;

public class Benchmark {

    public static void main(String[] args) throws IOException, RunnerException, URISyntaxException {

        Options opt = new OptionsBuilder()

                .include(DextormBench.class.getSimpleName())
                .forks(1)
                .build();
        new Runner(opt).run();

    }

}
