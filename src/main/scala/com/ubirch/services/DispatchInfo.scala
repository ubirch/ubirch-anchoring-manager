package com.ubirch.services

import java.io.{ FileNotFoundException, IOException }
import java.nio.file.{ Files, Paths }

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import com.ubirch.models.Dispatch
import com.ubirch.services.config.ConfigProvider
import com.ubirch.services.formats.{ DefaultJsonConverterService, JsonConverterService, JsonFormatsProvider }
import com.ubirch.ConfPaths.GenericConfPaths
import javax.inject._

import scala.collection.JavaConverters._

@Singleton
class DispatchInfo @Inject() (config: Config, jsonConverterService: JsonConverterService) extends LazyLogging {

  lazy val info: List[Dispatch] = loadInfo.map(x => toDispatch(x)).getOrElse(Nil)

  private val file: String = config.getString(GenericConfPaths.DISPATCH_INFO_PATH)

  private def loadInfo: Option[String] = {
    try {
      val path = Paths.get(file)

      if (!path.toFile.exists()) {
        throw new FileNotFoundException("file not found " + file)
      }

      val dispatch = Files.readAllLines(path).asScala
      val value = dispatch.mkString(" ")
      Some(value)

    } catch {
      case e: FileNotFoundException =>
        logger.error("Config dispatch info not found {}", e.getMessage)
        None
      case e: IOException =>
        logger.error("Error parsing into Config dispatch info {}", e.getMessage)
        None
    }
  }

  private def toDispatch(data: String): List[Dispatch] = {

    logger.debug(data.replace("\n", ""))

    jsonConverterService
      .as[List[Dispatch]](data)
      .map(data => {
        data.foreach { d =>
          if (d.name.isEmpty && d.name.length < 3) throw new IllegalArgumentException("Name can't be empty or has less than three letters")
          if (d.period < 1) throw new IllegalArgumentException("Period should be an Int greater to 0")
          if (d.topic.isEmpty && d.topic.length < 3) throw new IllegalArgumentException("Topic can't be empty or has less than three letters")
        }
        if (data.isEmpty) throw new IllegalArgumentException("No valid Dispatching was found.")
        data
      })
      .fold(e => {
        logger.error("Error parsing into Dispatch {}", e.getMessage)
        Nil
      }, data => data)

  }

}

object DispatchInfo {

  def main(args: Array[String]): Unit = {
    implicit val format = new JsonFormatsProvider().get()
    val di = new DispatchInfo(new ConfigProvider get (), new DefaultJsonConverterService {})
    println("Current dispatching rules: \n" + di.info.zipWithIndex.map { case (d, i) => (i, d) }.mkString(", \n"))

  }
}
