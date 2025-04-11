package com.nrin31266.shoppingapp.common

import com.nrin31266.shoppingapp.domain.model.BannerDataModel
import com.nrin31266.shoppingapp.domain.model.CategoryDataModel
import com.nrin31266.shoppingapp.domain.model.ProductDataModel

data class HomeScreenState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val categories: List<CategoryDataModel> = emptyList(),
    val products: List<ProductDataModel> = emptyList(),
    val banners: List<BannerDataModel> = emptyList()
)