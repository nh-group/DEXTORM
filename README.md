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

place a dextorm.properties files in the folder you are launching the JVM.

```ini
GRPCEndpointPort=8081
GRPCEndpointHost=localhost
InstrumentedPackage=fr.pantheonsorbonne.ufr27
sourceRootDir=src/main/java
ReqIssueDecorator=github
RemoteRepoIssues=https://github.com/nh-group/basic-cli-uni
RepoAddress=https://github.com/nh-group/basic-cli-uni
diffAlgorithm=GUMTREE
```