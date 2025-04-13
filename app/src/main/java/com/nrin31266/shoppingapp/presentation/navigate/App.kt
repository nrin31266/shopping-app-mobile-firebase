package com.nrin31266.shoppingapp.presentation.navigate

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
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
import com.google.firebase.auth.FirebaseAuth
import com.nrin31266.shoppingapp.R
import com.nrin31266.shoppingapp.presentation.LoginScreen
import com.nrin31266.shoppingapp.presentation.SignupScreen
import com.nrin31266.shoppingapp.presentation.screen.AllFavScreen
import com.nrin31266.shoppingapp.presentation.screen.CartScreen
import com.nrin31266.shoppingapp.presentation.screen.HomeScreen
import com.nrin31266.shoppingapp.presentation.screen.ProductDetailsScreen
import com.nrin31266.shoppingapp.presentation.screen.ProductsInCategoryScreen
import com.nrin31266.shoppingapp.presentation.screen.ProfileScreen

data class BottomNavItem(
    val name: String,

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
    var currentRoute = navBackStackEntry?.destination?.route
    val isShowBottomBar = remember { mutableStateOf(false) }

    LaunchedEffect(currentRoute) {
        isShowBottomBar.value = when (currentRoute) {
            Routers.LoginScreen.route, Routers.SignupScreen.route -> false
            else -> true
        }
    }

    val bottomNavItems = listOf(
        BottomNavItem("Home", Icons.Default.Home, Icons.Outlined.Home),
        BottomNavItem("Wishlist", Icons.Default.Favorite, Icons.Outlined.Favorite),
        BottomNavItem("Cart", Icons.Default.ShoppingCart, Icons.Outlined.ShoppingCart),
        BottomNavItem("Profile", Icons.Default.Person, Icons.Outlined.Person)
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
                        selectedItem = selectedItem,
                        itemSize = bottomNavItems.size,
                        modifier = Modifier,
                        containerColor = Color.Transparent,
                        indicatorColor = colorResource(id = R.color.elegant_gold),
                        indicatorDirection = IndicatorDirection.BOTTOM,
                        indicatorStyle = IndicatorStyle.FILLED
                    ) {
                        bottomNavItems.forEachIndexed { index, item ->
                            BottomBarItem(
                                selected = selectedItem == index,
                                onClick = {
                                    selectedItem = index
                                    when (index) {
                                        0 -> navController.navigate(Routers.HomeScreen.route)
                                        1 -> navController.navigate(Routers.WishlistScreen.route)
                                        2 -> navController.navigate(Routers.CartScreen.route)
                                        3 -> navController.navigate(Routers.ProfileScreen.route)
                                    }
                                },
                                label = item.name,
                                imageVector = if (selectedItem == index) item.icon else item.unSelectedIcon,
                                containerColor = Color.Transparent

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
                    .fillMaxSize()
                    .padding(bottom = if (isShowBottomBar.value) 60.dp else 0.dp)
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
                        ProductDetailsScreen(navController = navController, productId = productId)
                    }


                }
            }
        }
    )

}