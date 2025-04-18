package com.nrin31266.shoppingapp.presentation.navigate

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bottombar.AnimatedBottomBar
import com.example.bottombar.components.BottomBarItem
import com.example.bottombar.model.IndicatorDirection
import com.example.bottombar.model.IndicatorStyle
import com.example.bottombar.model.ItemStyle
import com.google.firebase.auth.FirebaseAuth
import com.nrin31266.shoppingapp.R
import com.nrin31266.shoppingapp.presentation.LoginScreen
import com.nrin31266.shoppingapp.presentation.SignupScreen
import com.nrin31266.shoppingapp.presentation.screen.AllFavScreen
import com.nrin31266.shoppingapp.presentation.screen.CartScreen
import com.nrin31266.shoppingapp.presentation.screen.CategoriesScreen
import com.nrin31266.shoppingapp.presentation.screen.CheckoutScreen
import com.nrin31266.shoppingapp.presentation.screen.HomeScreen
import com.nrin31266.shoppingapp.presentation.screen.ProductDetailsScreen
import com.nrin31266.shoppingapp.presentation.screen.ProductsInCategoryScreen
import com.nrin31266.shoppingapp.presentation.screen.ProductsScreen
import com.nrin31266.shoppingapp.presentation.screen.ProfileScreen


data class BottomNavItem(
    val name: String,
    val route: String,
    val icon: ImageVector,
    val unSelectedIcon: ImageVector,
)


@Composable
fun App(
    firebaseAuth: FirebaseAuth,
) {
    val navController = rememberNavController()
    var selectedItem by remember { mutableIntStateOf(0) }
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val isShowBottomBar = remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        isShowBottomBar.value = when (currentRoute) {
            Routers.HomeScreen.route, Routers.CartScreen.route,
                Routers.WishlistScreen.route, Routers.ProfileScreen.route-> true
            else -> false
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem("Home", Routers.HomeScreen.route, Icons.Default.Home, Icons.Outlined.Home),
        BottomNavItem("Wishlist", Routers.WishlistScreen.route, Icons.Default.Favorite, Icons.Outlined.FavoriteBorder),
        BottomNavItem("Cart", Routers.CartScreen.route, Icons.Default.ShoppingCart, Icons.Outlined.ShoppingCart),
        BottomNavItem("Profile", Routers.ProfileScreen.route, Icons.Default.Person, Icons.Outlined.Person)
    )


    val startScreen = if (firebaseAuth.currentUser != null) {
        SubNavigation.MainHomeScreen.route
    } else {
        SubNavigation.LoginSignupScreen.route
    }

    Scaffold(

        bottomBar = {
            if (isShowBottomBar.value) {
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            bottom = WindowInsets.navigationBars
                                .asPaddingValues()
                                .calculateBottomPadding()
                        ),


                    ) {
                    AnimatedBottomBar(
                        selectedItem = bottomNavItems.indexOfFirst { it.route == currentRoute },
                        itemSize = bottomNavItems.size,
                        modifier = Modifier,
                        containerColor = Color.Transparent,
                        indicatorColor = colorResource(id = R.color.elegant_gold),
                        indicatorDirection = IndicatorDirection.TOP,
                        indicatorStyle = IndicatorStyle.NONE,
                    ) {
                        bottomNavItems.forEach { item ->
                            BottomBarItem(
                                selected = item.route == currentRoute,
                                onClick = {
                                    if (currentRoute != item.route) {
                                        navController.navigate(item.route) {
                                            popUpTo(Routers.HomeScreen.route) { inclusive = false }
                                            launchSingleTop = true
                                        }
                                    }
                                },
                                label = item.name,
                                imageVector = if (item.route == currentRoute) item.icon else item.unSelectedIcon,
                                containerColor = Color.Transparent,
                                textColor = if (item.route == currentRoute) colorResource(id = R.color.light_background) else colorResource(id = R.color.dark_slate),
                                activeIndicatorColor = colorResource(id = R.color.elegant_gold),
                                itemStyle = ItemStyle.STYLE2,
                                iconColor = if (item.route == currentRoute) colorResource(id = R.color.light_background) else colorResource(id = R.color.dark_slate),
                            )
                        }
                    }


                }

            }
        },

        content = { innerPadding ->
            Box(
                modifier = Modifier
                    .padding(innerPadding)
//                    .padding(bottom = if (isShowBottomBar.value) 60.dp else 0.dp)
            ) {
                NavHost(navController = navController, startDestination = startScreen) {
                    navigation(
                        route = SubNavigation.LoginSignupScreen.route,
                        startDestination = Routers.LoginScreen.route,

                        ) {
                        composable(Routers.LoginScreen.route) {
                            LoginScreen(navController = navController)
                        }
                        composable(Routers.SignupScreen.route) {
                            SignupScreen(navController = navController)
                        }
                    }
                    navigation(
                        route = SubNavigation.MainHomeScreen.route,
                        startDestination = Routers.HomeScreen.route,
                    ) {
                        composable(Routers.HomeScreen.route) {
                            HomeScreen(navController = navController)
                        }

                        composable(Routers.ProfileScreen.route) {
                            ProfileScreen(
                                navController = navController,
                                firebaseAuth = firebaseAuth
                            )
                        }

                        composable(Routers.WishlistScreen.route) {
                            AllFavScreen(navController = navController)
                        }
                        composable(Routers.CartScreen.route) {
                            CartScreen(navController = navController)
                        }
                    }

                    composable(Routers.EachProductDetailsScreen.route
                    ,arguments = listOf(navArgument("productId") { type = NavType.StringType })) {
                        val productId = it.arguments?.getString("productId") ?: ""
                        ProductDetailsScreen(navController = navController, productId = productId)
                    }
                    composable(Routers.ProductsInCategoryScreen.route,
                        arguments = listOf(navArgument("categoryName") { type = NavType.StringType })) {
                        val categoryName = it.arguments?.getString("categoryName") ?: ""
                        ProductsInCategoryScreen(navController = navController, categoryName = categoryName)
                    }
                    composable(Routers.CheckoutScreen.route,
                        arguments = listOf(navArgument("productId") { type = NavType.StringType })) {
                        val productId = it.arguments?.getString("productId") ?: ""
                        CheckoutScreen(navController = navController, productId = productId)
                    }
                    composable(Routers.AllCategoriesScreen.route) {
                        CategoriesScreen(navController = navController)
                    }
                    composable(Routers.SeeAllProductScreen.route) {
                        ProductsScreen(navController = navController)

                    }


                }
            }
        }
    )

}