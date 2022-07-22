package com.scala.assignment6

import scala.collection.mutable

object MyDistributedKVStoreSolution {

  // Fake distribution: These are 4 mutable stores (Maps) on 4 machines. Please pretend that they
  // are located on different machines and that accessing them invokes network traffic.
  // Consider this when using them.
  val stores: Array[mutable.Map[String, String]] = Array(
    mutable.Map[String, String](), // Store 0 on machine 0.
    mutable.Map[String, String](), // Store 1 on machine 1.
    mutable.Map[String, String](), // Store 2 on machine 2.
    mutable.Map[String, String]() // Store 3 on machine 3.
  )

  def get(key: String): String = {
    val location = Math.abs(key.hashCode) % 4

    stores(location)(key)
  }

  def set(key: String, value: String): Unit = {
    val location = Math.abs(key.hashCode) % 4

    stores(location).put(key, value)
  }

  // Example usage:
  def main(args: Array[String]): Unit = {
    // Store.
    set("key1", "value1")
    set("key2", "value2")
    set("key3", "value3")

    // Get.
    assert(get("key1") == "value1")
    assert(get("key2") == "value2")
    assert(get("key3") == "value3")

    // Rapid store test.
    for (i <- 1 to 1000) set("key" + i, "value" + i)
    for (i <- 1 to 1000) assert(get("key" + i) == "value" + i)

    for(s <- stores) println("Size of store is: " + s.size)
  }

}
