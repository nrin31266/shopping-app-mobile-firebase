package com.nrin31266.shoppingapp.presentation.navigate

import kotlinx.serialization.Serializable

sealed class SubNavigation(val route: String) {

    data object LoginSignupScreen : SubNavigation("login_signup")
    data object MainHomeScreen : SubNavigation("main_home")
}

sealed class Routers(val route: String) {
    data object LoginScreen : Routers("login")
    data object SignupScreen : Routers("signup")
    data object HomeScreen : Routers("home")
    data object ProfileScreen : Routers("profile")
    data object WishlistScreen : Routers("wishlist")
    data object CartScreen : Routers("cart")
    data object PayScreen : Routers("pay")
    data object SeeAllProductScreen : Routers("see_all_products")
    data object AllCategoriesScreen : Routers("categories")

    data class CheckoutScreen(val productId: String) : Routers("checkout/$productId") {
        companion object {
            const val route = "checkout/{productId}"
        }
    }

    data class EachProductDetailsScreen(val productId: String) : Routers("product/$productId") {
        companion object {
            const val route = "product/{productId}"
        }
    }

    data class ProductsInCategoryScreen(val categoryName: String) : Routers("category/$categoryName") {
        companion object {
            const val route = "category/{categoryName}"
        }
    }
}