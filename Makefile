clean:
	mvn clean

build:
	mvn package

test:
	mvn test

run:	target/dextorm.jar
	cd target && ./run_app.sh

debug:	target/dextorm.jar
	cd target && ./run_app_debug.sh

target/dextorm.jar:
	mvn clean package