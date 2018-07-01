retrieveManaged := true

resolvers += "Spark Packages Repo" at "http://dl.bintray.com/spark-packages/maven"

name := "TrafficData"

version := "0.1"

scalaVersion := "2.11.11"

dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-core" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.core" % "jackson-databind" % "2.8.7"
dependencyOverrides += "com.fasterxml.jackson.module" % "jackson-module-scala_2.11" % "2.8.7"


libraryDependencies += "org.apache.spark" % "spark-core_2.11" % "2.2.0"

libraryDependencies += "org.apache.spark" % "spark-sql_2.11" % "2.2.0"

libraryDependencies += "org.apache.spark" % "spark-streaming_2.11" % "2.2.0"

libraryDependencies += "org.apache.spark" % "spark-mllib-local_2.11" % "2.2.0"

libraryDependencies += "dibbhatt" % "kafka-spark-consumer" % "1.0.12"

libraryDependencies += "org.apache.kafka" % "kafka_2.11" % "1.0.0"

// https://mvnrepository.com/artifact/org.mongodb.spark/mongo-spark-connector
libraryDependencies += "org.mongodb.spark" %% "mongo-spark-connector" % "2.2.1"

//libraryDependencies += "org.apache.spark" % "spark-streaming-kafka_2.11" % "1.6.3"

//use for import org.apache.spark.streaming.kafka.KafkaUtils
//libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-8_2.11" % "2.1.0"

libraryDependencies += "org.apache.spark" % "spark-streaming-kafka-0-10_2.11" % "2.1.0"
//libraryDependencies += "org.apache.kafka" % "kafka-streams" % "0.10.2.0"

