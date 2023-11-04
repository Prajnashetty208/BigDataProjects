package com.scala.assignment4

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.SparkSession

import scala.util.Random

object Task2 {

  def main(args: Array[String]): Unit = {

     //Setup spark.
    val spark = SparkSession
      .builder()
      .master("local[8]")  //Local Spark instance with 8 cores.
      .appName("Simple Application")
      .getOrCreate()

    val sc = spark.sparkContext

     //To get rid of extensive tracking messages (optional)
    sc.setLogLevel("ERROR")

        val random = new Random(123)
        for(_ <- 0 to 100){
          val string = random.nextString(100)
          assert(task4(sc.parallelize(string.toCharArray.map(x => x.toString).toSeq)) == string)
        }

     //The following are simple test cases to check you implementation.
     //Task 1:
    assert(task1(sc.parallelize(Seq(1, 2, 3))).collect().toSeq == Seq("odd", "even", "odd"))

    // Task 2:
    assert(task2(sc.parallelize(Seq("ClassA.scala", "ClassB.java", "readme.txt"))).collect().toSeq == Seq("ClassB.java"))

    // Task 3:
    assert(task3(sc.parallelize(Seq(1, 2, 3))) == 1 * 2 * 3)

    // Task 4: TODO: Does not work.
    assert(task4(sc.parallelize(Seq("a", "b", "c"))) == "abc")

    // Task 5:
    assert(task5(sc.parallelize(Seq("m1", "m2", "m3")), "m1"))
    assert(!task5(sc.parallelize(Seq("m1", "m2", "m3")), "m4"))

    // Task 6:
    assert(task6(sc.parallelize(Seq(("k1", "v1"), ("k1", "v2"), ("k2", "v1")))).collect().toMap == Map("k1" -> Seq("v1", "v2"), "k2" -> Seq("v1")))

    // Task 7:
    val l = sc.parallelize(Seq(("k1", "v1"), ("k2", "v2")))
    val r = sc.parallelize(Seq(("k2", "vv2"), ("k3", "vv2")))
    assert(task7(l, r).collect().toSeq == Seq(("k2", "v2", "vv2")))
  }

  /**
   * Return a list of strings "even" and "odd" for the input sequence of integers.
   */
  def task1(seq: RDD[Int]): RDD[String] = seq.map {
    case x if x % 2 == 0 => "even"
    case _ => "odd"
  }

  /**
   * Filter a list of path names for ".java" files.
   */
  def task2(seq: RDD[String]): RDD[String] =
    seq.filter(x => x.split("\\.").last == "java")

  /**
   * Form the product of all numbers.
   */
  def task3(seq: RDD[Int]): Int =
    seq.reduce((l, r) => l * r)

  /**
   * Concat the individual strings of seq to a single string.
   */
  def task4(seq: RDD[String]): String =  {
    seq.reduce((l, r) => l + r)
    seq.collect().reduce((l,r) => l + r)

    ???
  }  Does not work.


  /**
   * Check if x is a member (contained) in the sequence.
   */
  def task5(seq: RDD[String], x: String): Boolean =
    seq.map(m => m == x).fold(false)((l, r) => l || r)

  /**
   * Implement grouping by a key.
   */
  def task6(seq: RDD[(String, String)]): RDD[(String, Seq[String])] =
    seq.groupByKey().map { case (key, iterable) => (key, iterable.toSeq) }

  /**
   * Join the two sequences by the key (first entry of the tuple). Produce a 3-tuple containing
   * the results (key, left, right).
   * Hint: This is an advanced assignment, you may get additional points.
   */
  def task7(l: RDD[(String, String)], r: RDD[(String, String)]): RDD[(String, String, String)] = {
    l.join(r).map { case (a, (b, c)) => (a, b, c) }
  }

}