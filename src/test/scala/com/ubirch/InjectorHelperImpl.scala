package com.ubirch

import com.google.inject.binder.ScopedBindingBuilder
import com.typesafe.config.{ Config, ConfigValueFactory }
import com.ubirch.ConfPaths.{ AnchorConsumerConfPaths, AnchorProducerConfPaths }
import com.ubirch.services.config.ConfigProvider

/**
  * Customized Injector for changing values for tests
  * @param bootstrapServers Represents the bootstrap servers for kafka
  */
class InjectorHelperImpl(bootstrapServers: String) extends InjectorHelper(List(new Binder {
  override def Config: ScopedBindingBuilder = bind(classOf[Config])
    .toProvider(new ConfigProvider {
      override def conf: Config = {
        super.conf
          .withValue(AnchorConsumerConfPaths.BOOTSTRAP_SERVERS, ConfigValueFactory.fromAnyRef(bootstrapServers))
          .withValue(AnchorProducerConfPaths.BOOTSTRAP_SERVERS, ConfigValueFactory.fromAnyRef(bootstrapServers))
      }
    })
}))
