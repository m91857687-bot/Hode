package com.example.engine

import com.example.model.Country
import com.example.model.MarketCommodity
import com.example.model.ResourceType
import com.example.model.TradeContract

object TradeEngine {

    fun processMonthlyTradeContracts(
        contracts: List<TradeContract>,
        countries: Map<String, Country>
    ): Pair<List<TradeContract>, Map<String, Country>> {
        val activeContracts = mutableListOf<TradeContract>()
        val updatedCountries = countries.toMutableMap()

        for (c in contracts) {
            if (c.isExpired) continue

            val buyer = updatedCountries[c.buyerCountryId]
            val seller = updatedCountries[c.sellerCountryId]

            if (buyer != null && seller != null && !c.isPaused) {
                val costBillions = c.totalMonthlyBuyerCostMillions / 1000.0
                val sellerRevBillions = c.totalMonthlyVolumeCostMillions / 1000.0

                // Deduct cost from buyer and credit revenue to seller
                updatedCountries[buyer.id] = buyer.copy(
                    treasuryBillions = (buyer.treasuryBillions - costBillions).coerceAtLeast(0.0)
                )
                updatedCountries[seller.id] = seller.copy(
                    treasuryBillions = seller.treasuryBillions + sellerRevBillions
                )
            }

            val nextRemaining = c.remainingMonths - 1
            if (nextRemaining > 0) {
                activeContracts.add(c.copy(remainingMonths = nextRemaining))
            } else if (c.autoRenew) {
                // Renew contract
                activeContracts.add(c.copy(remainingMonths = c.durationMonths))
            }
        }

        return Pair(activeContracts, updatedCountries)
    }

    fun createTradeContract(
        buyerCountryId: String,
        sellerCountry: Country,
        resourceType: ResourceType,
        monthlyQuantity: Double,
        marketPrice: Double,
        durationMonths: Int,
        tariffPercent: Double = 0.05
    ): TradeContract {
        return TradeContract(
            id = "tc_${buyerCountryId}_${sellerCountry.id}_${System.currentTimeMillis() % 100000}",
            buyerCountryId = buyerCountryId,
            sellerCountryId = sellerCountry.id,
            resourceType = resourceType,
            monthlyQuantity = monthlyQuantity,
            agreedUnitPrice = marketPrice,
            durationMonths = durationMonths,
            remainingMonths = durationMonths,
            tariffPercent = tariffPercent,
            autoRenew = true
        )
    }

    fun buySpotCommodity(
        buyer: Country,
        resourceType: ResourceType,
        quantityThousands: Double,
        marketPrice: Double
    ): Country? {
        val totalCostBillions = ((quantityThousands * marketPrice) * 1.05) / 1_000_000.0
        if (buyer.treasuryBillions < totalCostBillions) return null

        val currentRes = buyer.resources[resourceType.name]
        val updatedRes = if (currentRes != null) {
            currentRes.copy(
                reservesUnits = currentRes.reservesUnits + (quantityThousands / 1000.0),
                currentProductionUnits = currentRes.currentProductionUnits + quantityThousands
            )
        } else {
            com.example.model.CountryResource(
                resourceType = resourceType,
                reservesUnits = quantityThousands / 1000.0,
                monthlyCapacityUnits = quantityThousands,
                currentProductionUnits = quantityThousands,
                extractionCostPerUnit = marketPrice
            )
        }

        val newResources = buyer.resources.toMutableMap()
        newResources[resourceType.name] = updatedRes

        return buyer.copy(
            treasuryBillions = buyer.treasuryBillions - totalCostBillions,
            resources = newResources
        )
    }
}
