package monocle.syntax

import monocle.{Lens, Optional, Prism}
import monocle.function.{At, Cons, Field1, Field2, Index}

object lens extends LensSyntax

trait LensSyntax {
  implicit class LensOps[A, B](optic: Lens[A, B]) {
    def _1(implicit ev: Field1[B]): Lens[A, ev.A] = first(ev)
    def _2(implicit ev: Field2[B]): Lens[A, ev.A] = second(ev)

    def at[I, C](i: I)(implicit ev: At.Aux[B, I, C]): Lens[A, Option[C]] =
      optic.compose(ev.at(i))

    def cons(implicit ev: Cons[B]): Optional[A, (ev.A, B)] =
      optic.compose(ev.cons)

    def first(implicit ev: Field1[B]): Lens[A, ev.A] =
      optic.compose(ev.first)

    def headOption(implicit ev: Cons[B]): Optional[A, ev.A] =
      optic.compose(ev.headOption)

    def index[I, C](i: I)(implicit ev: Index.Aux[B, I, C]): Optional[A, C] =
      optic.compose(ev.index(i))

    def left[E, C](implicit ev: B =:= Either[E, C]): Optional[A, E] =
      optic.asTarget[Either[E, C]].compose(Prism.left[E, C])

    def right[E, C](implicit ev: B =:= Either[E, C]): Optional[A, C] =
      optic.asTarget[Either[E, C]].compose(Prism.right[E, C])

    def second(implicit ev: Field2[B]): Lens[A, ev.A] =
      optic.compose(ev.second)

    def some[C](implicit ev: B =:= Option[C]): Optional[A, C] =
      optic.asTarget[Option[C]].compose(Prism.some[C])

    def tailOption(implicit ev: Cons[B]): Optional[A, B] =
      optic.compose(ev.tailOption)
  }
}

