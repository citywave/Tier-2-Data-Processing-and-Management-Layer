package io.github.omalperera

import com.mongodb.spark.MongoSpark
import org.apache.kafka.clients.consumer.{ConsumerConfig, ConsumerRecord}
import org.apache.kafka.clients.consumer.ConsumerRecord
import org.apache.kafka.common.serialization.StringDeserializer
import org.apache.spark.sql.{SQLContext, SaveMode}
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import org.apache.spark.streaming.kafka010._
import org.apache.spark.streaming.kafka010.LocationStrategies.PreferConsistent
import org.apache.spark.streaming.kafka010.ConsumerStrategies.Subscribe


//import org.apache.kafka.clients.consumer.ConsumerConfig



object vehicleSpeeds extends Serializable {

  // schema for vehicleSpeed data
  case class vehicleSpeeds(kmh: Int, startingCity: String, endingCity: String)

  // function to parse line of vehicalSpeed data into vehicalSpeed class
  def seperateRecord(line: String): vehicleSpeeds = {
    val dataItem = line.split(",")
    vehicleSpeeds(dataItem(0).toInt, dataItem(1), dataItem(2))
  }

  val timeout = 10 // Terminate after N seconds
  val batchSeconds = 2 // Size of batch intervals


  def main(args: Array[String]): Unit = {

    /*
    * Use the KafkaUtils createDirectStream method to create an input stream from a Kafka topic.
    * This creates a DStream that represents the stream of incoming data,
    * where each record is a line of text.
    */

    val batchInterval = "5"
    val brokers = "localhost:9092"
    val topics = "test"
    val groupId = "direct_Stream_Traffic_Data"
    val offsetReset = "earliest"
    val pollTimeout = "1000"


    //val sparkConf = new SparkConf().setMaster("local[*]").setAppName("vehicleSpeedStream")
    //val ssc = new StreamingContext(sparkConf, Seconds(batchInterval.toInt))
    val ssc = new StreamingContext(SparkCommon.conf, Seconds(batchInterval.toInt))

    // Create direct kafka stream with brokers and topics
    val topicsSet = topics.split(",").toSet

    val kafkaParams = Map[String, String](
      ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG -> brokers,
      ConsumerConfig.GROUP_ID_CONFIG -> groupId,
      ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
      ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG -> "org.apache.kafka.common.serialization.StringDeserializer",
      //ConsumerConfig.AUTO_OFFSET_RESET_CONFIG -> offsetReset,
      ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG -> "false",
      "spark.kafka.poll.time" -> pollTimeout
    )

    val messages = KafkaUtils.createDirectStream[String, String](ssc, PreferConsistent, Subscribe[String, String](topicsSet, kafkaParams))

    //val vehicleDStream = messages.map(_._2).map(seperateRecord)
    val vehicleDStream = messages.map(_.value).map(seperateRecord)
    //val words = vehicleDStream.flatMap(_.split(","))
    //words.print()
    //val speeds = Seq()
    //val topStudent = speeds.reduceLeft(max)
    //println(s"${topStudent.name} had the highest score: ${topStudent.score}")


    vehicleDStream.foreachRDD { rdd =>

      // There exists at least one element in RDD
      if (!rdd.isEmpty) {
        val count = rdd.count
        println("count received " + count)
        val sqlContext = SQLContext.getOrCreate(rdd.sparkContext)

        import sqlContext.implicits._
        import org.apache.spark.sql.functions._

        val vehicleSpeedDataFrame = rdd.toDF()
        // Display the top 20 rows of DataFrame
        //println("sensor data")
        //vehicleSpeedDataFrame.show()
        vehicleSpeedDataFrame.createOrReplaceTempView("sensor")     //registerTempTable("sensor")


        val result = sqlContext.sql("SELECT MIN(kmh) AS MINkmh, MAX(kmh) AS MAXkmh, AVG(kmh) AS AVGkmh FROM sensor")//("SELECT * FROM sensor")
        //println("-------------------------")
        result.show

        /*
        //count
        val res = sqlContext.sql("SELECT kmh, count(kmh) as total FROM sensor GROUP BY kmh")
        println("speed count ")
        res.show


        //Average
        val resultAverage = sqlContext.sql("SELECT kmh, AVG(kmh) as AVG_kmh FROM sensor GROUP BY kmh")
        println("Speed kmh average")
        resultAverage.show


        //Maximum
        val resultMaximum = sqlContext.sql("SELECT kmh, MAX(kmh) as MAX_kmh FROM sensor GROUP BY kmh")
        println("Maximum Speed kmh")
        resultMaximum.show


        //Minimum
        val resultMinimum = sqlContext.sql("SELECT kmh, MIN(kmh) as MIN_kmh FROM sensor GROUP BY kmh")
        println("Minimum Speed kmh")
        resultMinimum.show
        */



        //Insert data to MongoDB
        //MongoSpark.save(data.toDF().write.mode(SaveMode.Append))
        MongoSpark.save(result.toDF().write.mode(SaveMode.Append))


      }

    }

    // Start the computation
    ssc.start()
    ssc.awaitTermination()
  }
}