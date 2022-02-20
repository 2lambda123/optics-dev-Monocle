package monocle.macros.internal

import monocle.Lens
import monocle.macros.GenLens
import munit.DisciplineSuite

class ContextBoundCompilationIssueSpec extends DisciplineSuite {

  private trait Foo[T]
  private trait Bar[T]

  private case class A[T: Foo](s: A.S[T]) {
    val lens: Lens[A.S[T], Bar[T]] = GenLens[A.S[T]](_.bar)
  }

  private object A {
    case class S[T: Foo](bar: Bar[T])
  }

  private case object FooImpl extends Foo[Unit]
  private case object BarImpl extends Bar[Unit]

  private val a: A[Unit] = A(A.S(BarImpl)(FooImpl))(FooImpl)

  test("context.bound.compilation") {
    assertEquals(a.lens.get(a.s), BarImpl)
  }

}
