package com.nrin31266.shoppingapp.domain.model

data class CartDataModel(
    val productId: String = "",
    var name : String = "",
    var image : String = "",
    var price : String = "",
    var quantity: Int = 0,
    var cartId: String = "",
    var size: String = "",
    var description : String ="",
    var category: String= ""
)