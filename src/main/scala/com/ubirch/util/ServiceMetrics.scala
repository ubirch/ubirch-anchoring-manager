package com.ubirch.util

import io.prometheus.client.Counter

import scala.concurrent.{ ExecutionContext, Future }
import scala.util.{ Failure, Success }

trait ServiceMetrics {

  def service: String

  def successCounter: Counter

  def errorCounter: Counter

  def count[T](blockchain: String)(cf: Future[T])(implicit ec: ExecutionContext): Future[T] = {
    cf.onComplete {
      case Success(_) => successCounter.labels(service, blockchain).inc()
      case Failure(_) => errorCounter.labels(service, blockchain).inc()
    }
    cf
  }

}
