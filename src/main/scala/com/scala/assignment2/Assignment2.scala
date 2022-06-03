package com.bigdata.assignment.assignment2

import scala.util.Random
import scala.collection.mutable

object Assignment2 {

  // ------------------------ Start of the simulation code. ------------------------
  // Some flags.
  val FAILED = 0
  val READY = 1
  val PENDING = 2

  // A random number generator.
  var random = new Random(1234)

  // Variables that we use to track time and progress.
  var time = 0
  var completedTasks = 0

  // Number of machines that we have.
  var nMachines: Int = 10
  var todoTasks: Int = 20000
  var communication: Int = -100
  var failRate: Double = 0.01

  // Our abstracted cloud: number of tasks and done work on a certain machine.
  var cloud: mutable.Map[Int, (Int, Int)] = mutable.Map[Int, (Int, Int)]()

  for (i <- 0 until nMachines) cloud.put(i, (0, 0))

  // Simulation: Submit a number of tasks to a machine.
  def submit(machine: Int, nTasks: Int): Unit =
    cloud.put(machine, (nTasks, communication))

  // Simulation: Get the status of a machine (can be FAILED, READY or PENDING)
  def status(machine: Int): Int = {
    val (task, work) = cloud(machine)
    if (work == Int.MinValue) return FAILED
    if (work >= task) return READY
    PENDING
  }

  // Simulation: Run a single simulation step.
  def simulate(): Unit = {
    // Simulate one computation step on each machine.
    time = time + 1

    for (machine <- 0 until nMachines if status(machine) == PENDING) {
      // Simulate computation and transmission.
      val (task, work) = cloud(machine)

      cloud.put(machine, (task, work + 1))

      // Randomly fail at failRate of the computation steps.
      if (random.nextDouble() < failRate && work > 0)
        cloud.put(machine, (task, Int.MinValue))
      // If not failed, ddd the competed tasks if full batch is ready.
      else if (task == work + 1) completedTasks = completedTasks + task
    }
  }

  // Simulation: Reset simulation.
  def reset(seed: Int = 1234): Unit = {
    time = 0
    completedTasks = 0
    random = new Random(seed)
    cloud = mutable.Map[Int, (Int, Int)]()
    for (i <- 0 until nMachines) cloud.put(i, (0, 0))
  }

  // Simulation: Print current progress.
  def printProgress(): Unit =
    println("Completed " + completedTasks + " tasks out of " + todoTasks + " on time step " + time + ".")

  // ------------------------ End of the simulation code. ------------------------
  def main(args: Array[String]): Unit = {

    // TODO: Please improve the following (delete if necessary).

    for (j <- 1 to todoTasks) {
      while (completedTasks < todoTasks) {
        for (i <- 0 until nMachines) {
          if (status(i) == READY)
            submit(i, j)

          if (status(i) == FAILED)
            submit(i, j)
        }
        simulate()
      }
      printProgress()
    }

  }
}

