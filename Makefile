clean:
	mvn clean

build:
	mvn package assembly:single@make-bench

build-docker:
	docker build -f ./docker/Dockerfile . -t nherbaut/dextorm-benchmark
bench:
	cd target && java -jar ./benchmark.jar
	rm -rf /tmp/dextorm*

test:
	mvn test

run:	target/dextorm.jar
	cd target && ./run_app.sh

debug:	target/dextorm.jar
	cd target && ./run_app_debug.sh

target/dextorm.jar:
	mvn clean package
