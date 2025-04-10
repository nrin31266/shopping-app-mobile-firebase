package com.nrin31266.shoppingapp.common

import com.nrin31266.shoppingapp.domain.model.BannerDataModel
import com.nrin31266.shoppingapp.domain.model.CategoryDataModel

class HomeScreenState {
    val loading: Boolean = true
    val errorMessage: String? = null
    val categories: List<CategoryDataModel> = emptyList()
    val products: List<CategoryDataModel> = emptyList()
    val banners : List<BannerDataModel> = emptyList()

}