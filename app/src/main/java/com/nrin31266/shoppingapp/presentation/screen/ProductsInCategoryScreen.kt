package com.nrin31266.shoppingapp.presentation.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@Composable
fun ProductsInCategoryScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController,
    categoryName: String
) {

}