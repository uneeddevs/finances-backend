CC=mvn
SONAR_ARGS=clean verify test sonar:sonar -Dsonar.login=admin -Dsonar.password=admin1
TEST_ARGS=clean verify test
make:
	${CC} clean install package

sonar:
	${CC} ${SONAR_ARGS}

test:
	${CC} ${TEST_ARGS}