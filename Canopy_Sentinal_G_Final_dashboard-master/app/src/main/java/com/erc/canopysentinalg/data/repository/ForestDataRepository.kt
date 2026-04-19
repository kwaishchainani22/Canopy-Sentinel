package com.erc.canopysentinalg.data.repository

import com.erc.canopysentinalg.data.MockSatelliteDataProvider
import com.erc.canopysentinalg.data.model.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class ForestDataRepository {

    private val mockDataProvider = MockSatelliteDataProvider

    fun getForestStats(countryCode: String?): Flow<ForestStats> = flow {
        emit(mockDataProvider.getForestHealthStats(countryCode))
    }

    fun getAlerts(countryCode: String?, limit: Int): Flow<List<DeforestationAlert>> = flow {
        emit(mockDataProvider.generateMockAlerts(countryCode, limit))
    }

    fun getCountries(): Flow<List<Country>> = flow {
        emit(mockDataProvider.getCountries())
    }
    fun getCountryByCode(code: String): Flow<Country?> = flow {
        emit(
            mockDataProvider
                .getCountries()
                .find { it.code == code }
        )
    }
    fun getHistoricalStats(
        countryCode: String?,
        timeRange: TimeRange
    ): Flow<List<ForestStats>> = flow {
        emit(mockDataProvider.getHistoricalStats(countryCode, timeRange))
    }
}
