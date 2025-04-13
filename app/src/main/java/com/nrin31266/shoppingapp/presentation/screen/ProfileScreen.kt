package com.nrin31266.shoppingapp.presentation.screen

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@Composable
fun ProfileScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController,
    firebaseAuth: FirebaseAuth
) {
}