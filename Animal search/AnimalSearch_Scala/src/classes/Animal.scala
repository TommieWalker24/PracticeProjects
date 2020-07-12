package classes

import traits.{Classification, Diet, ScientificName}


class Animal ( var diet:Diet, var classification: Classification, var scientificName: ScientificName){

  override def toString(): String={
    s"""
       |Scientific Name: ${scientificName.scientificName}
       |Classification: ${classification.classification}
       |Diet: ${diet.diet}
       |""".stripMargin
  }


}
