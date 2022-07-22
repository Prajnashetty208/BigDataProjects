//package com.scala.assignment5
//
//import org.apache.spark.Partitioner
//import org.apache.spark.rdd.RDD
//import org.apache.spark.sql.SparkSession
//import org.apache.spark.util.Utils
//
//import scala.reflect.ClassTag
//
//object Task1 {
//
//  def nonNegativeMod(x: Int, mod: Int): Int = {
//    val rawMod = x % mod
//    rawMod + (if (rawMod < 0) mod else 0)
//  }
//
//  implicit class BetterIterable[K, V](val xs: Iterable[(K, V)]) {
//    def reduceByKey(op: (V, V) => V): Map[K, V] = xs.groupMapReduce(x => x._1)(x => x._2)(op)
//  }
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
//    val sc = spark.sparkContext
//
//    val l = sc.parallelize(Seq(("k1", "v1"), ("k2", "v2")))
//    val r = sc.parallelize(Seq(("k2", "vv2"), ("k3", "vv2")))
//
//    val out = myMap(l)(x => x)
//
//    println(out.collect().mkString("Array(", ", ", ")"))
//  }
//
//  def myFilter[A: ClassTag](xs: RDD[A])(f: A => Boolean): RDD[A] =
//    xs.mapPartitions { iter => iter.filter(f) }
//
//  def myMap[A: ClassTag, B: ClassTag](xs: RDD[A])(f: A => B): RDD[B] =
//    xs.mapPartitions { iter => iter.map(f) }
//
//  def myReduce[A: ClassTag](xs: RDD[A])(op: (A, A) => A): A = {
//    // First reduce on partitions.
//    val intermediate: RDD[A] = xs.mapPartitions(iter => Iterator(iter.reduce(op)))
//
//    // Collect.
//    val localAs: Array[A] = intermediate.collect()
//
//    // Reduce local.
//    localAs.reduce(op)
//  }
//
//  def myReduceByKey[K: ClassTag, V: ClassTag](xs: RDD[(K, V)])(op: (V, V) => V): RDD[(K, V)] = {
//
//    // Reduce within current partitioning.
//    val xsi: RDD[(K, V)] = xs.mapPartitions { iter => iter.toSeq.reduceByKey(op).iterator }
//
//    val myPartitioner = new Partitioner {
//      override def numPartitions: Int = 10
//
//      override def getPartition(key: Any): Int = nonNegativeMod(key.hashCode, numPartitions)
//    }
//
//    // Shuffle
//    val partitioned: RDD[(K, V)] = xsi.partitionBy(myPartitioner)
//
//    // Reduce remaining stuff.
//    partitioned.mapPartitions { iter => iter.toSeq.reduceByKey(op).iterator }
//
//  }
//
//  def myJoin[K: ClassTag, V1: ClassTag, V2: ClassTag](ls: RDD[(K, V1)], rs: RDD[(K, V2)]): RDD[(K, V1, V2)] = {
//
//    val myPartitioner: Partitioner = new Partitioner {
//      override def numPartitions: Int = 10
//
//      override def getPartition(key: Any): Int = nonNegativeMod(key.hashCode, numPartitions)
//    }
//
//    val lspartitioned = ls.partitionBy(myPartitioner)
//    val rspartitioned = rs.partitionBy(myPartitioner)
//
//    lspartitioned.zipPartitions(rspartitioned) { case (itl, itr) =>
//      val lss: Map[K, Seq[V1]] = itl.toSeq.groupMap(x => x._1)(x => x._2)
//      val rss: Map[K, Seq[V2]] = itr.toSeq.groupMap(x => x._1)(x => x._2)
//
//      lss.keySet.intersect(rss.keySet).iterator.flatMap { key =>
//        // Cartesian for those with the same key.
//        for (l <- lss(key); r <- rss(key)) yield (key, l, r)
//      }
//    }
//  }
//
//}
