package com.example

import org.junit.Assert.assertEquals
import org.junit.Test

class ExampleSanctuaryConfigTest {

  @Test
  fun sanctuaryIdentity_isValid() {
    val appName = "solace+"
    assertEquals("solace+", appName)
  }
}
