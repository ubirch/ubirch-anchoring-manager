package com.ubirch.models

import com.ubirch.TestBase

class DispatchSpec extends TestBase {

  "Dispatch mode" must {
    "normalizeName" in {
      val dispatch = Dispatch("Iota is-cool", 1, "topic")
      assert(dispatch.normalizeName == "Iota_is_cool")
    }
  }

}
