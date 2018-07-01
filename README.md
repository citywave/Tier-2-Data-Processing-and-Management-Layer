# Tier 2 - Data Processing and Management Layer

## Real-Time IoT Stream Processing Framework for Smart City Applications

<!--
Below is the architecture diagram for Real-Time IoT Stream Processing Framework for Smart City Applications.
 ![Framework Architecture](https://github.com/OmalPerera/SmartCity-TrafficData/blob/trafficdata/framework-Architecture.jpg) -->

## Purpose

Simulate the Tier 2 big data processing steps with **RDD, Data Frames(DF), Spark SQL** and save the output to a mongo database.

Also includes following features

- built in IoT data Simulator (vehucular data simulator)
- Spark - Apache Kafka Integration
- Spark - Mongo Integration

<br>

## Flow of data is as follows

1. "dataProducer_Vehicular.scala" generates random data at a given interval 
2. produced data in step 1 will be sent to a Apache Kafka topic (kafka address specified in "dataProducer_Vehicular.scala")
3. "vehicleSpeeds.scala" retrives data from kafka and do the big data processing (kafka client is defined in "vehicleSpeeds.scala")
4. results are sent to the mongo database (database is specified in "SparkCommon.scala")


<br>


## Priror to Running the component

1. Ensure you have scala 2.11.11 in your machine
2. Start the kafka in the host and port + create a topic match to topic in the "dataProducer_Vehicular.scala" and "vehicleSpeeds.scala" (https://omalperera.github.io/general/bigdata/2017/11/10/Setting-Up-Apache-Kafka-localy.html)
3. Start and run the mongo instance in a host:port matching to host:port in "SparkCommon.scala"
4. Then run the "dataProducer_Vehicular.scala" and "vehicleSpeeds.scala" seperatly one after one.




## Highlights

### RDD (Resilient Distributed Dataset)

At a high level, every Spark application consists of a driver program that runs the user’s main function and executes various parallel operations on a cluster. The main abstraction Spark provides is a resilient distributed dataset (RDD), which is a collection of elements partitioned across the nodes of the cluster that can be operated on in parallel. RDDs are created by starting with a file in the Hadoop file system (or any other Hadoop-supported file system), or an existing Scala collection in the driver program, and transforming it. Users may also ask Spark to persist an RDD in memory, allowing it to be reused efficiently across parallel operations. Finally, RDDs automatically recover from node failures
(from https://spark.apache.org/docs/latest/rdd-programming-guide.html)

<br>

### Datasets & DF (Data Frames)

A Dataset is a distributed collection of data. Dataset is a new interface added in Spark 1.6 that provides the benefits of RDDs (strong typing, ability to use powerful lambda functions) with the benefits of Spark SQL’s optimized execution engine. A Dataset can be constructed from JVM objects and then manipulated using functional transformations (map, flatMap, filter, etc.). The Dataset API is available in Scala and Java. Python does not have the support for the Dataset API. But due to Python’s dynamic nature, many of the benefits of the Dataset API are already available (i.e. you can access the field of a row by name naturally row.columnName). The case for R is similar.
(more : https://spark.apache.org/docs/latest/sql-programming-guide.html)

<br>

### Spark SQL

One use of Spark SQL is to execute SQL queries. Spark SQL can also be used to read data from an existing Hive installation. For more on how to configure this feature, please refer to the Hive Tables section. When running SQL from within another programming language the results will be returned as a Dataset/DataFrame. You can also interact with the SQL interface using the command-line or over JDBC/ODBC.
(more : https://spark.apache.org/docs/latest/sql-programming-guide.html#sql)


Author : Omal Perera (https://omalperera.github.io/)






