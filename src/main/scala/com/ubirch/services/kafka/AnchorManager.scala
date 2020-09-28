package com.ubirch
package services.kafka

import com.typesafe.config.Config
import com.typesafe.scalalogging.LazyLogging
import com.ubirch.ConfPaths.{ AnchorConsumerConfPaths, AnchorProducerConfPaths, GenericConfPaths }
import com.ubirch.kafka.consumer.WithConsumerShutdownHook
import com.ubirch.kafka.express.ExpressKafka
import com.ubirch.kafka.producer.WithProducerShutdownHook
import com.ubirch.services.DispatchInfo
import com.ubirch.services.lifeCycle.Lifecycle
import com.ubirch.util.ServiceMetrics
import io.prometheus.client.Counter
import javax.inject._
import org.apache.kafka.common.serialization._

import scala.concurrent.ExecutionContext
import scala.util.{ Failure, Success }

abstract class AnchorManager(val config: Config, lifecycle: Lifecycle)
  extends ExpressKafka[String, Array[Byte], Unit]
  with WithConsumerShutdownHook
  with WithProducerShutdownHook
  with ServiceMetrics
  with LazyLogging {

  override val service: String = config.getString(GenericConfPaths.NAME)

  override val successCounter: Counter = Counter.build()
    .name("anchoring_mgr_success")
    .help("Represents the number anchor management successes")
    .labelNames("service", "blockchain")
    .register()

  override val errorCounter: Counter = Counter.build()
    .name("anchoring_mgr_failures")
    .help("Represents the number of anchor management failures")
    .labelNames("service", "blockchain")
    .register()

  override val keyDeserializer: Deserializer[String] = new StringDeserializer
  override val valueDeserializer: Deserializer[Array[Byte]] = new ByteArrayDeserializer
  override val consumerTopics: Set[String] = Set(config.getString(AnchorConsumerConfPaths.IMPORT_TOPIC_PATH))
  override val keySerializer: Serializer[String] = new StringSerializer
  override val valueSerializer: Serializer[Array[Byte]] = new ByteArraySerializer
  override val consumerBootstrapServers: String = config.getString(AnchorConsumerConfPaths.BOOTSTRAP_SERVERS)
  override val consumerGroupId: String = config.getString(AnchorConsumerConfPaths.GROUP_ID_PATH)
  override val consumerMaxPollRecords: Int = config.getInt(AnchorConsumerConfPaths.MAX_POLL_RECORDS)
  override val consumerGracefulTimeout: Int = config.getInt(AnchorConsumerConfPaths.GRACEFUL_TIMEOUT_PATH)
  override val metricsSubNamespace: String = config.getString(AnchorConsumerConfPaths.METRICS_SUB_NAMESPACE)
  override val consumerReconnectBackoffMsConfig: Long = config.getLong(AnchorConsumerConfPaths.RECONNECT_BACKOFF_MS_CONFIG)
  override val consumerReconnectBackoffMaxMsConfig: Long = config.getLong(AnchorConsumerConfPaths.RECONNECT_BACKOFF_MAX_MS_CONFIG)
  override val maxTimeAggregationSeconds: Long = 120
  override val producerBootstrapServers: String = config.getString(AnchorProducerConfPaths.BOOTSTRAP_SERVERS)
  override val lingerMs: Int = config.getInt(AnchorProducerConfPaths.LINGER_MS)

  lifecycle.addStopHooks(hookFunc(consumerGracefulTimeout, consumption), hookFunc(production))

  def shouldSend(period: Int): Boolean

}

@Singleton
class DefaultAnchorManager @Inject() (
    dispatchInfo: DispatchInfo,
    config: Config,
    lifecycle: Lifecycle
)(implicit val ec: ExecutionContext) extends AnchorManager(config, lifecycle) {

  private var tickCounter: Int = 1

  override val process: Process = Process { crs =>

    crs.foreach { cr =>

      dispatchInfo.info.foreach { blockchain =>
        if (shouldSend(blockchain.period)) {

          val sent = count(blockchain.normalizeName)(send(blockchain.topic, cr.value()))

          sent.onComplete {
            case Failure(e) =>
              logger.error(s"Error sending to topic ${blockchain.topic} for blockchain ${blockchain.name} {}", e.getMessage)
            case Success(_) =>
              logger.debug(s"Dispatched to blockchain ${blockchain.name}")
          }
        }
      }
      tickCounter = tickCounter + 1
    }
  }

  override def shouldSend(period: Int): Boolean = tickCounter % period == 0

  override def prefix: String = "Ubirch"

}
