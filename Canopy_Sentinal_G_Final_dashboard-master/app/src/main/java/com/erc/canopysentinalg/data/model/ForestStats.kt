package com.erc.canopysentinalg.data.model

data class ForestStats(
    val totalArea: Double, // in hectares
    val deforestedArea: Double, // in hectares
    val forestHealthPercentage: Double,
    val alertsCount: Int,
    val lastUpdate: Long = System.currentTimeMillis()
) {
    val healthyArea: Double
        get() = totalArea - deforestedArea
    
    val deforestationRate: Double
        get() = if (totalArea > 0) (deforestedArea / totalArea) * 100 else 0.0
}
