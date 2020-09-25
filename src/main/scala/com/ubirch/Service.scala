package com.ubirch

import com.ubirch.services.kafka.AnchorManager

/**
  * Represents a bootable service object that starts the anchoring manager system
  */
object Service extends Boot(List(new Binder)) {
  def main(args: Array[String]): Unit = * {
    get[AnchorManager].start()
  }
}
