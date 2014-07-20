package monocle

import _root_.scalaz.Equal
import org.scalacheck.Prop._
import org.scalacheck.{Properties, Arbitrary}


object IsoLaws {

  def apply[S: Arbitrary: Equal, A: Arbitrary: Equal](iso: SimpleIso[S, A]) = new Properties("Iso") {

    import _root_.scalaz.syntax.equal._

    include(LensLaws(iso))
    include(PrismLaws(iso))

    property("double inverse") = forAll { (from: S, newValue: A) =>
      iso.reverse.reverse.get(from) === iso.get(from)
      iso.reverse.reverse.set(from, newValue) === iso.set(from, newValue)
    }

  }

}
