package com.ubirch
package services.kafka

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import com.ubirch.ConfPaths.{ TigerConsumerConfPaths, TigerProducerConfPaths }
import com.ubirch.kafka.consumer.WithConsumerShutdownHook
import com.ubirch.kafka.express.ExpressKafka
import com.ubirch.kafka.producer.WithProducerShutdownHook
import com.ubirch.services.lifeCycle.Lifecycle
import javax.inject._
import monix.execution.Scheduler
import org.apache.kafka.common.serialization._
import org.json4s.{ DefaultFormats, Formats }

import scala.concurrent.ExecutionContext

abstract class AnchorManager(val config: Config, lifecycle: Lifecycle)
  extends ExpressKafka[String, Array[Byte], Unit]
  with WithConsumerShutdownHook
  with WithProducerShutdownHook
  with LazyLogging {

  override val keyDeserializer: Deserializer[String] = new StringDeserializer
  override val valueDeserializer: Deserializer[Array[Byte]] = new ByteArrayDeserializer
  val importTopic: String = config.getString(TigerConsumerConfPaths.IMPORT_TOPIC_PATH)
  val activationTopic: String = config.getString(TigerConsumerConfPaths.ACTIVATION_TOPIC_PATH)
  override val consumerTopics: Set[String] = Set(importTopic, activationTopic)
  override val keySerializer: Serializer[String] = new StringSerializer
  override val valueSerializer: Serializer[Array[Byte]] = new ByteArraySerializer
  override val consumerBootstrapServers: String = config.getString(TigerConsumerConfPaths.BOOTSTRAP_SERVERS)
  override val consumerGroupId: String = config.getString(TigerConsumerConfPaths.GROUP_ID_PATH)
  override val consumerMaxPollRecords: Int = config.getInt(TigerConsumerConfPaths.MAX_POLL_RECORDS)
  override val consumerGracefulTimeout: Int = config.getInt(TigerConsumerConfPaths.GRACEFUL_TIMEOUT_PATH)
  override val metricsSubNamespace: String = config.getString(TigerConsumerConfPaths.METRICS_SUB_NAMESPACE)
  override val consumerReconnectBackoffMsConfig: Long = config.getLong(TigerConsumerConfPaths.RECONNECT_BACKOFF_MS_CONFIG)
  override val consumerReconnectBackoffMaxMsConfig: Long = config.getLong(TigerConsumerConfPaths.RECONNECT_BACKOFF_MAX_MS_CONFIG)
  override val maxTimeAggregationSeconds: Long = 120
  override val producerBootstrapServers: String = config.getString(TigerProducerConfPaths.BOOTSTRAP_SERVERS)
  override val lingerMs: Int = config.getInt(TigerProducerConfPaths.LINGER_MS)

  lifecycle.addStopHooks(hookFunc(consumerGracefulTimeout, consumption), hookFunc(production))

}

@Singleton
class DefaultAnchorManager @Inject() (
    config: Config,
    lifecycle: Lifecycle
)(implicit val ec: ExecutionContext, scheduler: Scheduler) extends AnchorManager(config, lifecycle) {

  implicit val formats: Formats = DefaultFormats

  override val process: Process = ???

  override def prefix: String = "Ubirch"

}
