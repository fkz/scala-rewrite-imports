import doesnotexist.h

object Q

object ja {
  object du { val x = 10 } 
  object du2 { val y = 20 } 
}

import hallo.du.x
import hallo.du2.y

object Main {
  def main(args: Array[String]): Unit = {
    println(x)
    println(y)
  }
}
