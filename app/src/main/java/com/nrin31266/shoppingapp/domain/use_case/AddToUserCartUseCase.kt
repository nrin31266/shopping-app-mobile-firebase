package com.nrin31266.shoppingapp.domain.use_case

import com.nrin31266.shoppingapp.common.ResultState
import com.nrin31266.shoppingapp.domain.model.CartDataModel
import com.nrin31266.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AddToUserCartUseCase @Inject constructor(private  val repo: Repo) {

    fun addToCart(cartDataModel: CartDataModel) : Flow<ResultState<String>>{

        return repo.addToCart(cartDataModel);
    }
}