package com.erc.canopysentinalg.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.erc.canopysentinalg.data.model.Country
import com.erc.canopysentinalg.data.model.DeforestationAlert
import com.erc.canopysentinalg.data.model.ForestStats
import com.erc.canopysentinalg.data.model.TimeRange
import com.erc.canopysentinalg.data.repository.ForestDataRepository
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class ForestViewModel(application: Application) : AndroidViewModel(application) {

    // --- Data Repository (Only one declaration allowed) ---
    private val repository = ForestDataRepository()

    // --- Observable Data States ---
    private val _forestStats = MutableLiveData<ForestStats?>()
    val forestStats: LiveData<ForestStats?> = _forestStats

    private val _alerts = MutableLiveData<List<DeforestationAlert>>()
    val alerts: LiveData<List<DeforestationAlert>> = _alerts

    private val _countries = MutableLiveData<List<Country>>()
    val countries: LiveData<List<Country>> = _countries

    private val _selectedCountry = MutableLiveData<Country?>()
    val selectedCountry: LiveData<Country?> = _selectedCountry

    private val _selectedTimeRange = MutableLiveData<TimeRange>(TimeRange.YEAR_1)
    val selectedTimeRange: LiveData<TimeRange> = _selectedTimeRange

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        loadCountries()
        loadForestStats()
        loadAlerts()
    }

    // --- NEW: Carbon Estimation Logic ---
    fun getCarbonEstimation(totalArea: Double, healthPercent: Double): Pair<Double, Double> {
        // Calculation: Area (ha) * 200 (MT/ha) * Health Factor
        val metricTons = totalArea * 200.0 * (healthPercent / 100.0)

        // Economic Value: Estimated $40 USD per metric ton carbon credit
        val financialValue = metricTons * 40.0

        return Pair(metricTons, financialValue)
    }

    // --- Data Loading Logic ---
    fun loadHistoricalStats(monthsAgo: Int) {
        val timeRange = when(monthsAgo) {
            0 -> TimeRange.MONTH_1
            3 -> TimeRange.MONTH_3
            6 -> TimeRange.MONTH_6
            else -> TimeRange.YEAR_1
        }

        viewModelScope.launch {
            _isLoading.value = true
            repository.getForestStats(_selectedCountry.value?.code)
                .collect { stats ->
                    // Simulation: Reduce health as we go back in time for visual effect
                    val simulatedStats = stats?.copy(
                        forestHealthPercentage = stats.forestHealthPercentage - (monthsAgo * 2)
                    )
                    _forestStats.value = simulatedStats
                    _isLoading.value = false
                }
        }
    }

    fun loadForestStats(countryCode: String? = null) {
        viewModelScope.launch {
            _isLoading.value = true
            repository.getForestStats(countryCode)
                .catch { e ->
                    _errorMessage.value = e.message
                    _isLoading.value = false
                }
                .collect { stats ->
                    _forestStats.value = stats
                    _isLoading.value = false
                }
        }
    }

    fun loadAlerts(countryCode: String? = null, limit: Int = 50) {
        viewModelScope.launch {
            repository.getAlerts(countryCode, limit)
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { alertsList ->
                    _alerts.value = alertsList
                }
        }
    }

    fun loadCountries() {
        viewModelScope.launch {
            repository.getCountries()
                .catch { e ->
                    _errorMessage.value = e.message
                }
                .collect { countriesList ->
                    _countries.value = countriesList
                }
        }
    }

    fun selectCountry(country: Country?) {
        _selectedCountry.value = country
        if (country == null) {
            loadForestStats(null)
            loadAlerts(null)
        } else {
            loadForestStats(country.code)
            loadAlerts(country.code)
        }
    }

    fun setTimeRange(timeRange: TimeRange) {
        _selectedTimeRange.value = timeRange
        loadForestStats(_selectedCountry.value?.code)
    }

    fun refreshData() {
        loadForestStats(_selectedCountry.value?.code)
        loadAlerts(_selectedCountry.value?.code)
    }

    fun clearError() {
        _errorMessage.value = null
    }
}