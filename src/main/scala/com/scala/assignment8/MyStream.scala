package com.scala.assignment8

import scala.collection.mutable
import scala.io.Source

object MyStream {

  def main(args: Array[String]): Unit = {

    // Our stream coming from the frank.txt file.
    val source = Source.fromFile("F://Projects/Spring Boot/Projects/data/frank.txt")
    for (paragraph <- source.getLines) {
      // Splitting the paragraph into words.
      val words = paragraph.replaceAll("[^A-Za-z0-9]", " ").split(" ")
      // Passing the words to the consumers of this stream.
      for(word <- words){
        consume1(word)
        consume2(word)
        consume3(word)
        consume4(word)
      }
    }
    println("total word count map = "+wordCount.toString())
    println("total words = "+totalCount)
    println("distinct words = "+distinctWords)
    println("longest word = "+currentLongest)
    println("most frequent word = "+frequentWord)
    println("mean = "+mean)
  }

  // Example (Task 1) --------------------------------------
  // TAG: This is the final total count for consumer method 1 (stored in variable totalCount).
  var totalCount = 0

  // TAG: This is the map used for storing distinct words
  // and variable for storing total distinct words count for consumer method 2 (stored in variable distinctWords).
  var map: mutable.Map[String, Int] = mutable.Map[String, Int] ()
  var distinctWords = 0

  // TAG: This is the map used for storing words and their count for consumer method 3 (stored in variable wordCount).
  var wordCount: mutable.Map[String, Int] = mutable.Map[String, Int] ()

  // TAG: This is the final longest word for consumer method 4 (stored in variable currentLongest).
  var currentLongest = ""

  // TAG: This is the most frequent word for consumer method 5 (stored in variable frequentWord).
  var frequentWord = ""

  // TAG: This is the total word length and mean length of the word for consumer method 6 (stored in variable mean).
  var wordLength = 0
  var mean = 0


  def consume1(word: String): Unit = {
    // TODO: Compute and store the number of total words in the text.
    totalCount = totalCount + 1
  }

  // Task 2 -----------------------------------------------
  def consume2(word: String): Unit = {
    // TODO: Compute the number of distinct words in the text.
    val count  = map.getOrElse(word, -1)
    if (count.intValue() > 0) map.put(word, count+1)
    else map.put(word, 1)
    distinctWords = map.toSeq.length
  }

  // Task 3 -----------------------------------------------
  def consume3(word: String): Unit = {
    // TODO: Compute and store the word-counts in the text.
    val count  = wordCount.getOrElse(word, -1)
    if (count.intValue() > 0) wordCount.put(word, count+1)
    else wordCount.put(word, 1)
  }

  // Task 4 -----------------------------------------------
  def consume4(word: String): Unit = {
    // TODO: Compute and store the longest word in the text.
    if (word.length > currentLongest.length) currentLongest = word
  }

  // Task 5 -----------------------------------------------
  def consume5(word: String): Unit = {
    // TODO: Compute and store the word that appears most often.
    frequentWord = map.max._1
  }

  // Task 6 -----------------------------------------------
  def consume6(word: String): Unit = {
    // TODO: Compute and store the mean length of the words.
    wordLength = wordLength + word.length
    mean = wordLength / totalCount
  }

}

