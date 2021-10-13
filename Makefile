CC=mvn
TEST_ARGS=clean verify test sonar:sonar -Dsonar.login=admin -Dsonar.password=admin1

make:
	${CC} clean install package

test:
	${CC} ${TEST_ARGS}