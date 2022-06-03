package com.bigdata.assignment
package assignment1

import java.io.FileWriter
import scala.collection.mutable
import scala.util.Random

object MapReduceSolution {

  /**
   * The following methods simulates fake "big data".
   * It draws a random word out of the list ("word_0", "word_1" ... "word_n").
   * The idx is taken as seed, to produce the same word for the same index when repeating the call.
   */
  def generator(idx: Long, n: Int): String = {
    // Initialize random with seed.
    val random = new Random(idx)

    // Produce a word with up to n different options.
    "word_" + random.nextInt(n)
  }

  /**
   * This is a small helper that takes two immutable maps and sums up the values for the corresponding keys.
   * You need it for the map-reduce solution.
   */
  def op(l: Map[String, Int], r: Map[String, Int]): Map[String, Int] = {
    (l.keySet ++ r.keySet).map(key =>
      (key, l.getOrElse(key, 0) + r.getOrElse(key, 0))
    ).toMap
  }

  def main(args: Array[String]): Unit = {
    val size = 100
    val n = 10
    val ourStream: Seq[String] = (0 to size).map(x => generator(x, n))

    // Hint: This is how to initialize and update an empty mutable Map (be aware of "import scala.collection.mutable"):
    // val ourMutableMap: mutable.Map[String, Int] = mutable.Map[String, Int]()
    // ourMutableMap.update("word_1",1)
    // ourMutableMap.update("word_2",4)

    // Hint: This is how to initialize and update an immutable Map:
    // var ourImmutableMap: Map[String, Int] = Map(("word_1",1))
    // ourImmutableMap = ourImmutableMap.updated("word_2",4)

    // Hint: This is basic code to dump to a csv file.
    // val fw = new FileWriter("output.csv")
    // for ((word, count) <- ourMap){
    //   fw.write(word + "," + count + "\n")
    // }
    // fw.flush()
    // fw.close()


    // Your solution ...
    // Below is the solution to the word count problem using Immutable Maps.
    var ourImmutableMap: Map[String, Int] = Map[String, Int]()
    //ourImmutableMap = ourStream.groupMapReduce(identity)(_ => 1)(_ + _)
    ourImmutableMap = ourStream.map((t => Map(t -> 1))).reduce((a,b)=> op(a,b))
    println(ourImmutableMap)

    //Dump to a csv file.
    val fw = new FileWriter("output2.csv")
    for ((word, count) <- ourImmutableMap) {
      fw.write(word + "," + count + "\n")
    }
    fw.flush()
    fw.close()
  }

}

