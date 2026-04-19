package com.erc.canopysentinalg.data

import com.erc.canopysentinalg.data.model.Country
import com.erc.canopysentinalg.data.model.DeforestationAlert
import com.erc.canopysentinalg.data.model.ForestStats
import com.erc.canopysentinalg.data.model.TimeRange
import java.text.SimpleDateFormat
import java.util.*
import kotlin.random.Random

object MockSatelliteDataProvider {

    private val random = Random.Default
    private val dateFormat = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.getDefault())

    private val REGIONS = listOf(
        "Amazon Rainforest", "Congo Basin", "Borneo Forest",
        "Sumatra Rainforest", "New Guinea Forest", "Siberian Taiga",
        "Canadian Boreal Forest", "Atlantic Forest", "Cerrado"
    )

    private val COUNTRIES = listOf(
        Country("BR", "Brazil", -14.2350, -51.9253, 5_000_000.0, 250_000.0, 85.0),
        Country("ID", "Indonesia", -0.7893, 113.9213, 1_200_000.0, 80_000.0, 78.0),
        Country("CD", "Democratic Republic of Congo", -4.0383, 21.7587, 1_500_000.0, 120_000.0, 75.0),
        Country("MY", "Malaysia", 4.2105, 101.9758, 200_000.0, 15_000.0, 80.0),
        Country("PG", "Papua New Guinea", -6.3150, 143.9555, 300_000.0, 20_000.0, 82.0),
        Country("RU", "Russia", 61.5240, 105.3188, 8_000_000.0, 300_000.0, 88.0),
        Country("CA", "Canada", 56.1304, -106.3468, 3_500_000.0, 50_000.0, 92.0),
        Country("US", "United States", 37.0902, -95.7129, 3_000_000.0, 40_000.0, 90.0),
        Country("CO", "Colombia", 4.5709, -74.2973, 600_000.0, 30_000.0, 83.0),
        Country("PE", "Peru", -9.1900, -75.0152, 700_000.0, 35_000.0, 81.0)
    )

    fun generateNDVI(): Double =
        0.1 + random.nextDouble() * 0.8

    fun generateMockAlerts(
        countryCode: String? = null,
        limit: Int = 5
    ): List<DeforestationAlert> {

        val filteredCountries = countryCode?.let {
            COUNTRIES.filter { c -> c.code == it }
        } ?: COUNTRIES

        return List(limit) { index ->
            val country = filteredCountries.random()
            val area = 1 + random.nextDouble() * 9
            val timestamp = System.currentTimeMillis() - random.nextLong(30) * 24 * 60 * 60 * 1000

            val severity = when {
                area > 8 -> DeforestationAlert.AlertSeverity.CRITICAL
                area > 5 -> DeforestationAlert.AlertSeverity.HIGH
                area > 2 -> DeforestationAlert.AlertSeverity.MEDIUM
                else -> DeforestationAlert.AlertSeverity.LOW
            }

            DeforestationAlert(
                id = "alert_$index",
                region = REGIONS.random(),
                country = country.name,
                area = area,
                timestamp = timestamp,
                latitude = country.latitude + random.nextDouble(-5.0, 5.0),
                longitude = country.longitude + random.nextDouble(-5.0, 5.0),
                ndviValue = generateNDVI(),
                severity = severity
            )
        }.sortedByDescending { it.timestamp }
    }

    fun getForestHealthStats(countryCode: String? = null): ForestStats {

        val selected = countryCode?.let {
            COUNTRIES.find { c -> c.code == it }
        }

        val total = selected?.totalForestArea ?: COUNTRIES.sumOf { it.totalForestArea }
        val deforested = selected?.deforestedArea ?: COUNTRIES.sumOf { it.deforestedArea }

        val health = (total - deforested) / total * 100

        return ForestStats(
            totalArea = total,
            deforestedArea = deforested,
            forestHealthPercentage = health,
            alertsCount = generateMockAlerts(countryCode, 10).size
        )
    }

    fun getCountries(): List<Country> = COUNTRIES

    fun getHistoricalStats(
        countryCode: String?,
        timeRange: TimeRange
    ): List<ForestStats> {

        val points = if (timeRange == TimeRange.ALL_TIME) 24 else timeRange.months

        return List(points) {
            val base = getForestHealthStats(countryCode)
            val drift = random.nextDouble(-1.0, 1.0)

            base.copy(
                deforestedArea = base.deforestedArea + drift * 1000,
                forestHealthPercentage = base.forestHealthPercentage + drift,
                lastUpdate = System.currentTimeMillis() - it * 30L * 24 * 60 * 60 * 1000
            )
        }.sortedBy { it.lastUpdate }
    }
}
