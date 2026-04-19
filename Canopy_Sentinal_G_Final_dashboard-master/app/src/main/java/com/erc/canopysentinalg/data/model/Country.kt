package com.erc.canopysentinalg.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Country(
    val code: String,
    val name: String,
    val latitude: Double,
    val longitude: Double,
    val totalForestArea: Double = 0.0, // in hectares
    val deforestedArea: Double = 0.0, // in hectares
    val forestHealthPercentage: Double = 0.0
) : Parcelable
