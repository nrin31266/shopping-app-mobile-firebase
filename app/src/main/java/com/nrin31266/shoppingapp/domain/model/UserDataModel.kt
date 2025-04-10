package com.nrin31266.shoppingapp.domain.model

import androidx.compose.runtime.mutableStateMapOf

data class UserDataModel(
    val fullName: String = "",
    val email: String = "",
    val password: String = "",

    val profileImage: String = "",


    ) {
    fun toMap(): Map<String, Any> {
        return mutableStateMapOf(
            "fullName" to fullName,
            "email" to email,
            "password" to password,
            "profileImage" to profileImage
        )

    }
}

data class UserDataParent(
    val nodeId : String = "",
    val userData: UserDataModel
)