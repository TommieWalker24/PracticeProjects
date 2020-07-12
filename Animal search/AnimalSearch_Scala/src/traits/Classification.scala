package traits

sealed trait Classification{
  val classification: String;
  var animals: List[String]
}
case object Aves extends Classification {
  override val classification: String = "Aves"
  override var animals: List[String] = List()
}
case object Mammalia extends Classification {
  override val classification: String = "Mammalia"
  override var animals: List[String] = List()
}
case object Reptilia extends Classification {
  override val classification: String = "Reptalia"
  override var animals: List[String] = List()
}
