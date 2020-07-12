package traits

sealed trait Diet{
  var diet: String;
}
case object Herbivore extends Diet {
  override var diet: String = "Herbivore"
}
case object Carnivore extends Diet {
  override var diet: String = "Carnivore"
}
case object Omnivore extends Diet {
  override var diet: String = "Omnivore"
}
