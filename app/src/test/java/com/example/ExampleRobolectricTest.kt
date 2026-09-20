package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import org.junit.Assert.assertEquals
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("إدارة الدول: Statecraft", appName)
  }

  @Test
  fun `test game engine monthly simulation and budget`() {
    val countries = com.example.data.WorldData.getInitialCountries()
    val saudi = countries["SA"]
    org.junit.Assert.assertNotNull(saudi)

    val companies = com.example.data.WorldData.getInitialCompanies("SA")
    org.junit.Assert.assertTrue(companies.isNotEmpty())

    val budget = com.example.engine.GameEngine.calculateMonthlyBudget(saudi!!, companies, emptyList())
    org.junit.Assert.assertTrue(budget.totalRevenue > 0.0)
    org.junit.Assert.assertTrue(budget.totalExpense > 0.0)

    val initialGdp = saudi.gdpBillions
    val state = com.example.model.GameState(
        playerCountryId = "SA",
        countries = countries,
        companies = companies,
        gameMonth = 1,
        gameYear = 2026
    )

    val nextState = com.example.engine.GameEngine.processMonthlyTick(state)
    org.junit.Assert.assertEquals(2, nextState.gameMonth)
    org.junit.Assert.assertEquals(2026, nextState.gameYear)
    val nextSaudi = nextState.countries["SA"]
    org.junit.Assert.assertNotNull(nextSaudi)
    org.junit.Assert.assertTrue(nextSaudi!!.gdpBillions >= initialGdp)
  }
}
