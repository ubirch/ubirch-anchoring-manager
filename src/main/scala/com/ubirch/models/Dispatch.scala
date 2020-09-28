package com.ubirch.models

case class Dispatch(name: String, period: Int, topic: String) {
  def normalizeName: String = name.replace(" ", "_").replace("-", "_")
}
