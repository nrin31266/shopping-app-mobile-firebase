package com.nrin31266.shoppingapp.presentation.navigate

import kotlinx.serialization.Serializable

sealed class SubNavigation{

    @Serializable
    object LoginSignupScreen: SubNavigation()
    @Serializable
    object MainHomeScreen : SubNavigation()


}

sealed class Routers{
    @Serializable
    object LoginScreen

    @Serializable
    object SignupScreen

    @Serializable
    object HomeScreen

    @Serializable
    object ProfileScreen

    @Serializable
    object WishlistScreen

    @Serializable
    object CartScreen

    @Serializable
    data class CheckoutScreen(val productId : String)

    @Serializable
    object PayScreen

    @Serializable
    object SeeAllProductScreen

    @Serializable
    data class EachProductDetailsScreen(val productId: String)

    @Serializable
    object  AllCategoriesScreen

    @Serializable
    data class EachCategoryItemsScreen(val categoryName : String)



}