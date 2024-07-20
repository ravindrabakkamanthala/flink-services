# Flink Streaming Application.

Simple Flink Streaming Application that uses Kafka as Source and writes to SQL database.

The Flink Application Pipeline has 3 processes.
 1. Listen to Kafka Source
 2. Process the Data
 3. Sink the data to DB.

# Dev Setup

## Running through Docker

1. Install docker
   1. brew install docker
   2. brew install docker-compose
   3. Setup Docker Desktop
2. Run the below command to run the application in docker
   1. `docker-compose up --build jobmanager`
3. Once, the docker-compose is up, access the Flink Application from `http://localhost:8081`

## Running through IDE - For Debugging

1. Update the pom.xml flinkRunType to compile in properties.
2. Run the required dependencies through docker `docker-compose up --build kafka postgres`
3. Run the Application.java

## Produce Kafka Messages for Testing

1. Use KafkaProducerTest.java for producing some test messages to Kafka.