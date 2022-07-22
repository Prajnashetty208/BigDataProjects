//package com.scala.assignment4
//
//import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.SparkSession
//
//import scala.util.Random
//
//object Task1 {
//
//  def main(args: Array[String]): Unit = {
//    // Setup spark.
//    val spark = SparkSession
//      .builder()
//      .master("local[8]") // Local Spark instance with 8 cores.
//      .appName("Simple Application")
//      .getOrCreate()
//
//    // We start with plain Spark (sql will follow later).
//    val sc = spark.sparkContext
//
//    // To get rid of extensive tracking messages (optional)
//    sc.setLogLevel("ERROR")
//
//    // Our local data to start with.
//    val input: RDD[String] = sc.textFile("data/frank.txt")
//
//    // TODO: Count the words and drop to a local file.
//
//    val words: RDD[String] = input
//      .flatMap(paragraph => paragraph.replaceAll("[^A-Za-z0-9]", " ").split(" "))
//
//    val wordsWithoutEmpty: RDD[String] = words.filter(x => x != "")
//
//    val wordCountPairs: RDD[(String, Int)] = wordsWithoutEmpty.map(word => (word, 1))
//
//    val wordTotalCount: RDD[(String, Int)] = wordCountPairs.reduceByKey((l, r) => l + r)
//
//    val sorted = wordTotalCount.sortBy { case (word, count) => count }
//
//    for ((word, count) <- sorted.collect()) {
//      println(word + " -> " + count)
//    }
//
//    // Stop spark.
//    spark.stop()
//  }
//}
