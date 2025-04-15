package com.nrin31266.shoppingapp.presentation.screen

import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@Composable
fun HomeScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.profileScreenState.collectAsStateWithLifecycle()
    state.value.userData?.userData?.let { Text(it.fullName) }

}