package com

import scala.util.control.NoStackTrace

package object ubirch {

  abstract class ServiceException(message: String) extends Exception(message) with NoStackTrace {
    val name: String = this.getClass.getCanonicalName
  }

}
