package monocle.function

import monocle.Iso

import scala.annotation.implicitNotFound

/**
  * Typeclass that defines an [[Iso]] from an `S` to an `A` where `S` is expected to wrap `A`.
  * @tparam S source of [[Iso]]
  * @tparam A target of [[Iso]], `A` is supposed to be unique for a given `S`
  */
@implicitNotFound("Could not find an instance of Wrapped[${S},${A}], please check Monocle instance location policy to " +
  "find out which import is necessary")
abstract class Wrapped[S, A] extends Serializable {
  def wrapped: Iso[S, A]
}

trait WrappedFunctions {
  def wrapped[S, A](implicit ev: Wrapped[S, A]): Iso[S, A] = ev.wrapped
  def unwrapped[S, A](implicit ev: Wrapped[S, A]): Iso[A, S] = ev.wrapped.reverse
}

object Wrapped extends WrappedFunctions {

  def apply[S, A](iso: Iso[S, A]): Wrapped[S, A] = new Wrapped[S, A] {
    override val wrapped: Iso[S, A] = iso
  }

  import scalaz.{@@, Tag}

  implicit def tagWrapped[A, B]: Wrapped[A @@ B, A] = Wrapped(
    Iso(Tag.unwrap[A, B])(Tag.apply)
  )
}