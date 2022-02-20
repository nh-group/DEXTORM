# DEXTORM

## Build and Run

### Build

`mvn package`

### Running the app on the test app (basic-uni-cli)

```
cd target
./run_app.sh
```

### Running the app on the test app (basic-uni-cli) and wait for a debugger to connect

```
cd target
./run_app_debug.sh
```

## Configuration

place a dextorm.yaml files in the folder you are launching the JVM.

```yaml
app:
  instrumentedPackage: "fr.pantheonsorbonne.ufr27"
  sourceRootDir: src/main/java
  publisher: grpc1
  issueCollector: github1
  diffAlgorithm: GUMTREE
issueCollectors:
  github:
    github1:
      repo: nh-group/basic-cli-uni
publishers:
  grpcPublishers:
    grpc1:
      host: localhost
      port: 8081

  consolePublishers:
    console1: []

```
