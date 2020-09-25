package com.ubirch.services.formats

import javax.inject._
import org.json4s.{ DefaultFormats, Formats }
import org.json4s.ext.{ JavaTypesSerializers, JodaTimeSerializers }

/**
  * Represents a Json Formats Provider
  */
@Singleton
class JsonFormatsProvider extends Provider[Formats] {

  private val formats: Formats = DefaultFormats.lossless ++ JavaTypesSerializers.all ++ JodaTimeSerializers.all
  override def get(): Formats = formats

}
