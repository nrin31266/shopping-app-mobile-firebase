package com.nrin31266.shoppingapp.domain.model

data class BannerDataModel(
    val name: String = "",
    val image: String = "",
    val date: Long = System.currentTimeMillis()
)
