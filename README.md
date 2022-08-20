# DEXTORM

## Requirements

maven 3.6.3+, java 11+

## Building

```make build```

## Running Benchmarks

```make bench```

or 

```bash
docker build -f ./docker/Dockerfile . -t nherbaut/dextorm-benchmark
docker run nherbaut/dextorm-benchmark
```

## Usage

to run dextorm, pass a yaml configuration file.

```bash
java -jar ./dextorm.jar dextorm_config.yaml
```

### Configuration File

To configure dextorm, you need to provide some configuration parameters and the folder where it will fetch the coverage data.
Please take a look at the `src/main/resources/benchmark` folder for several examples.

```yaml
app:
  diffAlgorithm: gmethods
  instrumentedPackage: org.xbill.DNS
  issueCollector: dummy
  publisher: console1
  sourceRootDirs:
    - src/main/java
  coverageFolder: benchmark/dnsjava/coverage
differs:
  gmethods:
    diffAlgorithm: GUMTREE
    instructions: false
    methods: true
issueCollectors:
  github:
    dummy:
      gitHubRepoName: dnsjava/dnsjava
publishers:
  filePublishers:
    console1:
      filePath: stdout
```