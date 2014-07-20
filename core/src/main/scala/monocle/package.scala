import _root_.scalaz.{ -\/, \/- }
import scala.util.Try

package object monocle {

  type SimpleSetter[S, A]    = Setter[S, S, A, A]
  type SimpleTraversal[S, A] = Traversal[S, S, A, A]
  type SimpleLens[S, A]      = Lens[S, S, A, A]
  type SimpleIso[S, A]       = Iso[S, S, A, A]
  type SimpleOptional[S, A]  = Optional[S, S, A, A]
  type SimplePrism[S, A]     = Prism[S, S, A, A]

  object SimpleLens {
    def apply[S, A](_get: S => A, _set: (S, A) => S): SimpleLens[S, A] =
      Lens[S, S, A, A](_get, _set)

    /** Alternative syntax that allows the field type to be inferred rather and explicitly specified. */
    def apply[S]: Constructor[S] = new Constructor[S]
    final class Constructor[S] {
      @inline def apply[A](_get: S => A)(_set: (S, A) => S): SimpleLens[S, A] = Lens[S, S, A, A](_get, _set)
    }
  }

  object SimpleOptional {
    def apply[S, A](_getOption: S => Option[A], _set: (S, A) => S): SimpleOptional[S, A] =
      Optional[S, S, A, A](_getOption, {
        case (s, Some(a)) => _set(s, a)
        case (s, None)    => s
      })

    /** Alternative syntax that allows the field type to be inferred rather and explicitly specified. */
    def apply[S]: Constructor[S] = new Constructor[S]
    final class Constructor[S] {
      @inline def apply[A](_getOption: S => Option[A])(_set: (S, A) => S): SimpleOptional[S, A] =
        SimpleOptional(_getOption, _set)
    }
  }

  object SimpleIso {
    def apply[S, A](_get: S => A, _reverseGet: A => S): SimpleIso[S, A] = Iso(_get, _reverseGet)
    def dummy[S]: SimpleIso[S, S] = SimpleIso(identity, identity)

    /** Alternative syntax that allows the field type to be inferred rather and explicitly specified. */
    def apply[S]: Constructor[S] = new Constructor[S]
    final class Constructor[S] {
      @inline def apply[A](_get: S => A)(_reverseGet: A => S): SimpleIso[S, A] = Iso[S, S, A, A](_get, _reverseGet)
    }
  }

  object SimplePrism {
    def apply[S, A](_reverseGet: A => S, _getOption: S => Option[A]): SimplePrism[S, A] =
      Prism(_reverseGet, { s: S => _getOption(s).map(\/-(_)) getOrElse -\/(s) })

    def trySimplePrism[S, A](safe: A => S, unsafe: S => A): SimplePrism[S, A] =
      SimplePrism(safe, s => Try(unsafe(s)).toOption)

    /** Alternative syntax that allows the field type to be inferred rather and explicitly specified. */
    def apply[A]: Constructor[A] = new Constructor[A]
    final class Constructor[A] {
      @inline def apply[S](_reverseGet: A => S)(_getOption: S => Option[A]) = SimplePrism[S, A](_reverseGet, _getOption)
      //@inline def rev[B](_getOption: A => Option[B])(_reverseGet: B => A) = SimplePrism[A, B](_reverseGet, _getOption)
    }
  }

  implicit final class SimplePrismOps[S, A](prism: SimplePrism[S, A]){
    def reverseModify(from: A, f: S => S): Option[A] =  prism.getOption(f(prism.reverseGet(from)))
  }

}
