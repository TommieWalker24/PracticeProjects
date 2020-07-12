package objects

import java.io.FileInputStream

import classes.Animal
import org.apache.poi.ss.usermodel.{Row, WorkbookFactory}
import traits.{Aves, Carnivore, Classification, Diet, Herbivore, Mammalia, Omnivore, Reptilia, ScientificName}

import scala.collection.mutable


object Excel{
  val fileName = "../animal_listing.xlsx"
  val hashMap = new mutable.HashMap[String, Animal]()

  def buildInfo(): mutable.HashMap[String, Animal] = {
    try {
      val inputStream = new FileInputStream(fileName)
      val workbooks = WorkbookFactory.create(inputStream)

      try {
        val currentSheet = workbooks.getSheet("Sheet1")
        var animalCommonName : String = null;
        var animalClass:Classification = null
        var animalDiet:Diet = null
        var animalScienceName:ScientificName = null

        for(r<- 1 to currentSheet.getPhysicalNumberOfRows -1;c <- 0 to 3){
          c match{
            case 0=>{
              animalCommonName = currentSheet.getRow(r).getCell(c).getStringCellValue
            }
            case 1 =>{
              currentSheet.getRow(r).getCell(c).getStringCellValue match{
                case "aves"=>{
                  animalClass = Aves
                  Aves.animals = Aves.animals:::(animalCommonName::Nil)
                }
                case "reptilia" => {
                  animalClass = Reptilia
                  Reptilia.animals = Reptilia.animals:::(animalCommonName::Nil)
                }
                case "mammalia" => {
                  animalClass = Mammalia
                  Mammalia.animals = Mammalia.animals:::(animalCommonName::Nil)
                }
              }

            }
            case 2 => {
              currentSheet.getRow(r).getCell(c).getStringCellValue match{
                case "herbivore" => {
                  animalDiet = Herbivore
                  Herbivore.animals = Herbivore.animals:::(animalCommonName::Nil)
                }
                case "omnivore" => {
                  animalDiet = Omnivore
                  Omnivore.animals = Omnivore.animals:::(animalCommonName::Nil)
                }
                case "carnivore" => {
                  animalDiet = Carnivore
                  Carnivore.animals = Carnivore.animals:::(animalCommonName::Nil)
                }
                case _ => {
                  println("not valid diet")
                }
              }
            }
            case 3 => {
              animalScienceName = new ScientificName {
                override var scientificName: String = currentSheet.getRow(r).getCell(c).getStringCellValue
              }
            }
          }
          if(c == 3){
            var animal = new Animal(animalDiet, animalClass, animalScienceName)
            hashMap.addOne(animalCommonName, animal)
          }

        }

      } catch {
        case e: Exception =>
          e.printStackTrace()
      } finally if (workbooks != null) workbooks.close()
      inputStream.close()
    }

    catch{
      case e: Exception=>
        e.printStackTrace()
    }
    hashMap
  }

  def searchName(search:String):Animal = {
   hashMap.apply(search)
  }

  def allOfClass(search:String) = {
    search match{
      case "aves"=>{
        for(x <- Aves.animals){
          println(searchName(x).toString())
        }
    }
      case "reptilia"=> {
        for (x <- Reptilia.animals) {
          println(searchName(x).toString())
        }
      }
        case "mammalia"=>{
          for(x <- Mammalia.animals){
            println(searchName(x).toString())
        }
      }
    }
  }
  def allOfDiet(search:String) = {
    search match{
      case "omnivore"=>{
        for(x <- Omnivore.animals){
          println(searchName(x).toString())
        }
      }
      case "herbivore"=> {
        for (x <- Herbivore.animals) {
          println(searchName(x).toString())
        }
      }
      case "carnivore"=>{
        for(x <- Carnivore.animals){
          println(searchName(x).toString())
        }
      }
    }
  }



}
