package com.ubirch.services.kafka

import com.ubirch.kafka.util.PortGiver
import com.ubirch.services.config.DispatchInfo
import com.ubirch.{ InjectorHelperImpl, TestBase }
import net.manub.embeddedkafka.{ EmbeddedKafka, EmbeddedKafkaConfig }

class AnchoringManagerSpec extends TestBase with EmbeddedKafka {

  "The Anchoring Manager " must {
    "trigger dispatch when period matches tick" in {

      implicit lazy val kafkaConfig: EmbeddedKafkaConfig = EmbeddedKafkaConfig(kafkaPort = PortGiver.giveMeKafkaPort, zooKeeperPort = PortGiver.giveMeZookeeperPort)

      lazy val bootstrapServers = "localhost:" + kafkaConfig.kafkaPort
      lazy val Injector = new InjectorHelperImpl(bootstrapServers) {}
      val info = Injector.get[DispatchInfo].info
      val mgt = Injector.get[AnchorManager]

      withRunningKafka {

        mgt.consumption.startPolling()

        publishStringMessageToKafka("com.ubirch.anchoring_management", "one_message")
        publishStringMessageToKafka("com.ubirch.anchoring_management", "one_two")
        publishStringMessageToKafka("com.ubirch.anchoring_management", "one_three")
        publishStringMessageToKafka("com.ubirch.anchoring_management", "one_four")

        info match {
          case List(x, y) =>
            consumeNumberStringMessagesFrom(x.topic, 4)
            consumeNumberStringMessagesFrom(y.topic, 1)
          case _ =>
            fail("there are more blockchain configured")
        }

      }

    }
  }

}
