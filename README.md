# DEXTORM

Software artefacts of our short paper: "Automated and Robust User Story Coverage."

Citation: 

```Gudin, Mickael, and Nicolas Herbaut. "Automated and Robust User Story Coverage." In International Conference on Product-Focused Software Process Improvement, pp. 538-543. Cham: Springer International Publishing, 2022.```

Bibtex:

```bibtex
@inproceedings{DBLP:conf/profes/GudinH22,
  author       = {Mickael Gudin and
                  Nicolas Herbaut},
  title        = {Automated and Robust User Story Coverage},
  booktitle    = {Product-Focused Software Process Improvement - 23rd International
                  Conference, {PROFES} 2022, Jyv{\"{a}}skyl{\"{a}}, Finland,
                  November 21-23, 2022, Proceedings},
  series       = {Lecture Notes in Computer Science},
  volume       = {13709},
  pages        = {538--543},
  publisher    = {Springer},
  year         = {2022},
  doi          = {10.1007/978-3-031-21388-5\_39},
}
```

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
## Running the project on dextorm-dummy-project

```make clean run```

should generate the file `target/dextorm-vscode-export.json` containing the json export usable for IDE integration

## Usage

to run dextorm, pass a yaml configuration file.

```bash
java -jar ./dextorm.jar dextorm_config.yaml
```

### Configuration File

#### Automatic configuration
You can use the copanion repo [dextorm-onboarding](https://github.com/nh-group/dextorm-onboarding) to automatically configure and run dextorm on your project (subject to conditions)

#### Manual configuration
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
  ideToolExportPath: /home/nherbaut/Desktop/toto.json
differs:
  gmethods:
    diffAlgorithm: GUMTREE #GUMTREE or GITBLAME
    instructions: true #run the diff algorithm on instructions 
    methods: false #run the diff algorithm on method signature
issueCollectors:
  github:
    dummy:
      gitHubRepoName: dnsjava/dnsjava # url of the github repositorry
publishers:
  filePublishers:
    console1:
      filePath: stdout # where to show the output of dextorm while it runs
```
