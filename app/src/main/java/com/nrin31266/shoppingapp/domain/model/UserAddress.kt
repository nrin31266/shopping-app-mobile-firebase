package com.nrin31266.shoppingapp.domain.model

data class UserAddress(
    val name : String = "",
    val phoneNumber : String = "",
    val postalCode : String = "",
    val province : String = "",
    val district : String = "",
    val ward : String = "",
    val street : String = ""
)
