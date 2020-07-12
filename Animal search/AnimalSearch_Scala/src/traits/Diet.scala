package traits

sealed trait Diet{
  val diet: String;
  var animals:List[String]
}
case object Herbivore extends Diet {
  override val diet: String = "Herbivore"
  override var animals: List[String] = Nil
}
case object Carnivore extends Diet {
  override val diet: String = "Carnivore"
  override var animals: List[String] = Nil
}
case object Omnivore extends Diet {
  override val diet: String = "Omnivore"
  override var animals: List[String] = Nil
}
