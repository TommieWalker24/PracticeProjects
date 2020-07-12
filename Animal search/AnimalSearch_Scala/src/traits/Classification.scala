package traits

sealed trait Classification{
  var classification: String;
}
case object Aves extends Classification {
  override var classification: String = "Aves"
}
case object Mammalia extends Classification {
  override var classification: String = "Mammalia"
}
case object Reptilia extends Classification {
  override var classification: String = "Reptalia"
}
