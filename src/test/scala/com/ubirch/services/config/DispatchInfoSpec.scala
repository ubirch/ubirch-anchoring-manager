package com.ubirch.services.config

import com.ubirch.InjectorHelper.InjectionException
import com.ubirch.models.Dispatch
import com.ubirch.services.formats.JsonConverterService
import com.ubirch.{ InjectorHelperImpl, TestBase }

class DispatchInfoSpec extends TestBase {

  "The Dispatch info " must {
    "parse dispatch infos" in {

      val Injector = new InjectorHelperImpl("", Some("src/test/resources/DispatchTest.json")) {}
      val jsonConverter = Injector.get[JsonConverterService]
      val info = Injector.get[DispatchInfo].info

      val expected =
        """
          |[
          |  {
          |    "name": "Iota",
          |    "period": 1,
          |    "topic": "iota.topic"
          |  },
          |  {
          |    "name": "Ethereum",
          |    "period": 2,
          |    "topic": "etc.topic"
          |  }
          |]
          |""".stripMargin

      val parsed = jsonConverter.as[List[Dispatch]](expected).fold(e => fail(e), l => l)
      assert(info == parsed)

    }

    "break if one or more periods are not valid" in {

      val Injector = new InjectorHelperImpl("", Some("src/test/resources/DispatchTestFailedOnPeriod.json")) {}

      assertThrows[InjectionException] {
        Injector.get[DispatchInfo].info
      }

    }

    "break if one or more topic are not valid" in {

      val Injector = new InjectorHelperImpl("", Some("src/test/resources/DispatchTestFailedOnTopic.json")) {}

      assertThrows[InjectionException] {
        Injector.get[DispatchInfo].info
      }

    }

    "break if empty" in {

      val Injector = new InjectorHelperImpl("", Some("src/test/resources/DispatchTestFailedOnEmpty.json")) {}

      assertThrows[InjectionException] {
        Injector.get[DispatchInfo].info
      }

    }

    "break if not found" in {

      val Injector = new InjectorHelperImpl("", Some("src/test/resources/DispatchTestFailedOnNotFound.json")) {}

      assertThrows[InjectionException] {
        Injector.get[DispatchInfo].info
      }

    }

    "break if invalid" in {

      val Injector = new InjectorHelperImpl("", Some("src/test/resources/DispatchTestFailedOnInvalid.json")) {}

      assertThrows[InjectionException] {
        Injector.get[DispatchInfo].info
      }

    }

  }

}

