sudo apt-get install mvn
mvn clean compile assembly:single
java -jar target/TPBioJac1-1.0-SNAPSHOT-jar-with-dependencies.jar
