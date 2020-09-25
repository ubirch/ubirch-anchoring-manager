package com.ubirch

/**
  * Object that contains configuration keys
  */
object ConfPaths {

  trait GenericConfPaths {
    final val NAME = "anchoringManager.name"
    final val DISPATCH_INFO_PATH = "anchoringManager.dispatching.file"
  }
  trait ExecutionContextConfPaths {
    final val THREAD_POOL_SIZE = "anchoringManager.executionContext.threadPoolSize"
  }

  trait AnchorConsumerConfPaths {
    final val BOOTSTRAP_SERVERS = "anchoringManager.kafkaConsumer.bootstrapServers"
    final val IMPORT_TOPIC_PATH = "anchoringManager.kafkaConsumer.import"
    final val ACTIVATION_TOPIC_PATH = "anchoringManager.kafkaConsumer.activation"
    final val MAX_POLL_RECORDS = "anchoringManager.kafkaConsumer.maxPollRecords"
    final val GROUP_ID_PATH = "anchoringManager.kafkaConsumer.groupId"
    final val GRACEFUL_TIMEOUT_PATH = "anchoringManager.kafkaConsumer.gracefulTimeout"
    final val METRICS_SUB_NAMESPACE = "anchoringManager.kafkaConsumer.metricsSubNamespace"
    final val FETCH_MAX_BYTES_CONFIG = "anchoringManager.kafkaConsumer.fetchMaxBytesConfig"
    final val MAX_PARTITION_FETCH_BYTES_CONFIG = "anchoringManager.kafkaConsumer.maxPartitionFetchBytesConfig"
    final val RECONNECT_BACKOFF_MS_CONFIG = "anchoringManager.kafkaConsumer.reconnectBackoffMsConfig"
    final val RECONNECT_BACKOFF_MAX_MS_CONFIG = "anchoringManager.kafkaConsumer.reconnectBackoffMaxMsConfig"
  }

  trait AnchorProducerConfPaths {
    final val LINGER_MS = "anchoringManager.kafkaProducer.lingerMS"
    final val BOOTSTRAP_SERVERS = "anchoringManager.kafkaProducer.bootstrapServers"
    final val ERROR_TOPIC_PATH = "anchoringManager.kafkaProducer.errorTopic"
  }

  trait PrometheusConfPaths {
    final val PORT = "anchoringManager.metrics.prometheus.port"
  }

  object GenericConfPaths extends GenericConfPaths
  object AnchorConsumerConfPaths extends AnchorConsumerConfPaths
  object AnchorProducerConfPaths extends AnchorProducerConfPaths

}
