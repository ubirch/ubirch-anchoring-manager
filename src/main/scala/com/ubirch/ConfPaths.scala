package com.ubirch

/**
  * Object that contains configuration keys
  */
object ConfPaths {

  trait GenericConfPaths {
    final val NAME = "anchoringManager.name"
  }
  trait ExecutionContextConfPaths {
    final val THREAD_POOL_SIZE = "anchoringManager.executionContext.threadPoolSize"
  }

  trait TigerConsumerConfPaths {
    final val BOOTSTRAP_SERVERS = "anchoringManager.tiger.kafkaConsumer.bootstrapServers"
    final val IMPORT_TOPIC_PATH = "anchoringManager.tiger.kafkaConsumer.import"
    final val ACTIVATION_TOPIC_PATH = "anchoringManager.tiger.kafkaConsumer.activation"
    final val MAX_POLL_RECORDS = "anchoringManager.tiger.kafkaConsumer.maxPollRecords"
    final val GROUP_ID_PATH = "anchoringManager.tiger.kafkaConsumer.groupId"
    final val GRACEFUL_TIMEOUT_PATH = "anchoringManager.tiger.kafkaConsumer.gracefulTimeout"
    final val METRICS_SUB_NAMESPACE = "anchoringManager.tiger.kafkaConsumer.metricsSubNamespace"
    final val FETCH_MAX_BYTES_CONFIG = "anchoringManager.tiger.kafkaConsumer.fetchMaxBytesConfig"
    final val MAX_PARTITION_FETCH_BYTES_CONFIG = "anchoringManager.tiger.kafkaConsumer.maxPartitionFetchBytesConfig"
    final val RECONNECT_BACKOFF_MS_CONFIG = "anchoringManager.tiger.kafkaConsumer.reconnectBackoffMsConfig"
    final val RECONNECT_BACKOFF_MAX_MS_CONFIG = "anchoringManager.tiger.kafkaConsumer.reconnectBackoffMaxMsConfig"
  }

  trait TigerProducerConfPaths {
    final val LINGER_MS = "anchoringManager.tiger.kafkaProducer.lingerMS"
    final val BOOTSTRAP_SERVERS = "anchoringManager.tiger.kafkaProducer.bootstrapServers"
    final val ERROR_TOPIC_PATH = "anchoringManager.tiger.kafkaProducer.errorTopic"
  }

  trait PrometheusConfPaths {
    final val PORT = "anchoringManager.metrics.prometheus.port"
  }

  object GenericConfPaths extends GenericConfPaths
  object TigerConsumerConfPaths extends TigerConsumerConfPaths
  object TigerProducerConfPaths extends TigerProducerConfPaths

}
