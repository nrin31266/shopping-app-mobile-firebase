package com.nrin31266.shoppingapp.domain.repo

import android.net.Uri
import com.nrin31266.shoppingapp.common.ResultState
import com.nrin31266.shoppingapp.domain.model.BannerDataModel
import com.nrin31266.shoppingapp.domain.model.CartDataModel
import com.nrin31266.shoppingapp.domain.model.CategoryDataModel
import com.nrin31266.shoppingapp.domain.model.ProductDataModel
import com.nrin31266.shoppingapp.domain.model.UserDataModel
import com.nrin31266.shoppingapp.domain.model.UserDataParent
import kotlinx.coroutines.flow.Flow


interface Repo  {
    fun signupUser(userDataModel: UserDataModel) : Flow<ResultState<String>>
    fun loginUser(email: String, password: String) : Flow<ResultState<String>>
    fun getUserDataById(userId: String) : Flow<ResultState<UserDataParent>>
    fun updateUserData(userDataParent: UserDataParent) : Flow<ResultState<String>>
    fun userProfileImage(uri: Uri) : Flow<ResultState<String>>
    fun getCategoriesInLimited(categoryName : String): Flow<ResultState<List<ProductDataModel>>>
    fun getProductsInLimited(): Flow<ResultState<List<ProductDataModel>>>
    fun getAllProducts(): Flow<ResultState<List<ProductDataModel>>>
    fun getProductById(productId: String): Flow<ResultState<ProductDataModel>>
    fun addToCart(cartDataModel: CartDataModel): Flow<ResultState<String>>
    fun addToFavourites(productDataModel: ProductDataModel): Flow<ResultState<String>>
    fun getAllFavourites(): Flow<ResultState<List<ProductDataModel>>>
    fun getCart(): Flow<ResultState<List<CartDataModel>>>
    fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>>
    fun getCheckout(productId: String):Flow<ResultState<ProductDataModel>>
    fun getBanner():Flow<ResultState<List<BannerDataModel>>>
    fun getSpecificCategoryItem(categoryName: String): Flow<ResultState<List<ProductDataModel>>>
    fun getAllSuggestedProducts(): Flow<ResultState<List<ProductDataModel>>>
}