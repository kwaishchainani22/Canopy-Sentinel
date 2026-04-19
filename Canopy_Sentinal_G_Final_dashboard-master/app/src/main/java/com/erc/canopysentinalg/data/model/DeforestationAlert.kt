package com.erc.canopysentinalg.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class DeforestationAlert(
    val id: String,
    val region: String,
    val country: String? = null,
    val area: Double, // in hectares
    val timestamp: Long,
    val latitude: Double,
    val longitude: Double,
    val ndviValue: Double,
    val severity: AlertSeverity = AlertSeverity.MEDIUM,
    val imageUrl: String? = null
) : Parcelable {
    enum class AlertSeverity {
        LOW, MEDIUM, HIGH, CRITICAL
    }
    
    val notificationMessage: String
        get() = "🚨 Deforestation Detected in $region! Estimated loss: ${String.format("%.1f", area)} ha"
}
