//package com.scala.assignment5
//
//import org.apache.spark.Partitioner
//import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.{DataFrame, SparkSession}
//
//import scala.reflect.{ClassTag, classTag}
//
//object Task2 {
//
//  def main(args: Array[String]): Unit = {
//
//    // Setup spark.
//    val spark = SparkSession
//      .builder()
//      .master("local[8]") // Local Spark instance with 8 cores.
//      .appName("Simple Application")
//      .getOrCreate()
//
//    spark.sparkContext.setLogLevel("ERROR")
//
//    // Needed to access the relevant spark SQL methods.
//    import spark.implicits._
//    import org.apache.spark.sql.functions._
//
//    val df: DataFrame = spark.read.format("csv")
//      .option("header", "true")
//      .load("data/movies.csv")
//
//    val counts = df.groupBy(col("original_language")).count().sort(col("count") * -1)
//
//    val top10 = counts.take(10).map(x => x.getString(0))
//
//    val filtered = df.where(array_contains(lit(top10), col("original_language")))
//
//    val countryGenres = filtered
//      .groupBy(col("genres"))
//      .pivot(col("original_language")).count()
//      .na.fill(0)
//      .sort(col("en") * -1)
//
//    val out = filtered.groupBy(col("genres"), col("original_language")).count()
//
//    val outWithTotal = out
//      .join(counts.withColumnRenamed("count", "total"), usingColumns = Seq("original_language"))
//      .withColumn("fraction", round(col("count") / col("total"), 2))
//
//    val in2d = outWithTotal
//      .groupBy(col("genres"))
//      .pivot("original_language").max("fraction")
//      .na.fill(0)
//      .sort(col("en") * -1)
//
//  }
//
//
//}
