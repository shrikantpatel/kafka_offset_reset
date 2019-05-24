This is springboot app

1. Build the project
gradle bootRepackage


2. Run the jar created in above step using this command, the --spring.config.location points to the SSL configuration, sample file is application.properties in
resource directory, you also move some of commandline parameter to that file.
java -jar kafka_offset_reset.jar --server.port=0 --topic=topic1 --bootstrap.server=localhost:9092 --consumer.group=test1 --partition.number=-1 --offset.value=0 --spring.config.location=C:/SHRIKANT/DEVELOPMENT/JAVA/PROJECT/kafka-related/kafka_offset_reset/src/main/resources/application-PDX.properties

3. Make sure any other running consumer in above consumer group is shutdown before you run the command.