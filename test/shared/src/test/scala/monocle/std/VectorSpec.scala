package monocle.std

import monocle.MonocleSuite
import monocle.function.Plated._
import monocle.law.discipline.function._
import monocle.law.discipline.TraversalTests

import scala.annotation.nowarn

class VectorSpec extends MonocleSuite {
  checkAll("reverse Vector", ReverseTests[Vector[Int]]): @nowarn
  checkAll("empty Vector", EmptyTests[Vector[Int]]): @nowarn
  checkAll("cons Vector", ConsTests[Vector[Int], Int])
  checkAll("snoc Vector", SnocTests[Vector[Int], Int])
  checkAll("each Vector", EachTests[Vector[Int], Int])
  checkAll("index Vector", IndexTests[Vector[Int], Int, Int])
  checkAll("filterIndex Vector", FilterIndexTests[Vector[Int], Int, Int])

  checkAll("plated Vector", TraversalTests(plate[Vector[Int]]))
}
