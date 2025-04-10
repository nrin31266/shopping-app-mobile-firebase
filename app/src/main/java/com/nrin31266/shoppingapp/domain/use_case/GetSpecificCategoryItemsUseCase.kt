package com.nrin31266.shoppingapp.domain.use_case

import com.nrin31266.shoppingapp.common.ResultState
import com.nrin31266.shoppingapp.domain.model.CategoryDataModel
import com.nrin31266.shoppingapp.domain.model.ProductDataModel
import com.nrin31266.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetSpecificCategoryItemsUseCase @Inject constructor(private val repo: Repo) {
    fun getSpecificCategoryItems(categoryName : String) : Flow<ResultState<List<ProductDataModel>>>{
        return repo.getCategoriesInLimited(categoryName)
    }

}