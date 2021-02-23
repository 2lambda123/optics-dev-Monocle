package monocle.function

import monocle.MonocleSuite

import scala.annotation.nowarn

@nowarn
class FieldsExample extends MonocleSuite {
  test("_1 creates a Lens from a 2-6 tuple to its first element") {
    assertEquals(("Hello", 3).optics.composeLens(first).get, "Hello")

    assertEquals(("Hello", 3, true).optics.composeLens(first).replace("World"), (("World", 3, true)))

    assertEquals(("Hello", 3, true, 5.6, 7L, 'c').optics.composeLens(first).get, "Hello")
  }

  test("_2 creates a Lens from a 2-6 tuple to its second element") {
    assertEquals(("Hello", 3).optics.composeLens(second).get, 3)

    assertEquals(
      ("Hello", 3, true, 5.6, 7L, 'c').optics.composeLens(second).replace(4),
      (("Hello", 4, true, 5.6, 7L, 'c'))
    )
  }

  // ...

  test("_6 creates a Lens from a 6-tuple to its sixth element") {
    assertEquals(("Hello", 3, true, 5.6, 7L, 'c').optics.composeLens(sixth).get, 'c')

    assertEquals(
      ("Hello", 3, true, 5.6, 7L, 'c').optics.composeLens(sixth).replace('a'),
      (("Hello", 3, true, 5.6, 7L, 'a'))
    )
  }
}
