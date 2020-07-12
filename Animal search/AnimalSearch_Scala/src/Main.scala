import objects.Excel

import scala.io.StdIn.readLine

object Main extends App {
  Excel.buildInfo()
  while(true){
    print(
      """
        |Below are your selection options, please select the number corresponding to your desired action.
        |1) Search an animals' name
        |2) Search an animals' class
        |3) Search an animals' type of diet
        |4) Exit this program
        |""".stripMargin)
    var selection = readLine()
    try {
      Integer.parseInt(selection) match {
        case 1 => {print("type in the animal name you would like to find information on: \n")
          print(Excel.searchName(readLine()).toString())
        }
        case 2 => {
          println("please enter the class of animal you are trying to find:")
          Excel.allOfClass(readLine().toLowerCase())
        }
        case 3 => {
          println("Please enter the diet type of the animal you are trying to find:")
          Excel.allOfDiet(readLine().toLowerCase())
        }
        case 4 => println("Exiting Program")
          System.exit(0);
        case _ => "Invalid Selection"
      }
    }catch {
      case e: Error => print(e)
      case e: Exception=> println(e)
    }

  }


}
