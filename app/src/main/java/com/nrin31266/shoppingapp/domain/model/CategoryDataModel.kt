package com.nrin31266.shoppingapp.domain.model

data class CategoryDataModel (
    val name: String = "",
    val image: String = "",
    val createdBy: String = "",
    val date: Long = System.currentTimeMillis()

)