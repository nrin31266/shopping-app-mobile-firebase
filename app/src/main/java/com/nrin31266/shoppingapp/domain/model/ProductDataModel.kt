package com.nrin31266.shoppingapp.domain.model


data class ProductDataModel(
    val name: String = "",
    val image: String = "",
    val description: String = "",
    val category: String = "",
    val mrpPrice: String = "",
    val sellingPrice: String = "",
    val createdBy: String = "",
    val date: Long = System.currentTimeMillis(),
    val availableUnits: Int = 0,
    val productId : String = "",
)
