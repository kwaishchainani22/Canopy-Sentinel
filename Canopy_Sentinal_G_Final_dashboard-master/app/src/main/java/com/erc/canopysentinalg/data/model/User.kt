package com.erc.canopysentinalg.data.model

data class User(
    val id: String,
    val name: String?,
    val email: String?,
    val photoUrl: String?,
    val isGuest: Boolean = false
)
