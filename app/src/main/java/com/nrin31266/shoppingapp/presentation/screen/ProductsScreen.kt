package com.nrin31266.shoppingapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.nrin31266.shoppingapp.presentation.navigate.Routers
import com.nrin31266.shoppingapp.presentation.utils.BackButton
import com.nrin31266.shoppingapp.presentation.utils.FullScreenLoading
import com.nrin31266.shoppingapp.presentation.utils.ProductItem
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductsScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.getAllProductsState.collectAsStateWithLifecycle()
    val products = state.value.products ?: emptyList()
    LaunchedEffect(
        key1 = Unit,

        ) {
        viewModel.getAllProducts()
    }

    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Products",
                        style = MaterialTheme.typography.headlineMedium,
                        fontWeight = FontWeight.Bold
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    BackButton(navController)
                }
            )
        },
        contentWindowInsets = WindowInsets(0)

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding).padding(8.dp)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, "") }
            )
            when {
                state.value.isLoading -> {
                    FullScreenLoading()
                }

                state.value.errorMessage != null -> {
                    Text(
                        text = state.value.errorMessage.toString(),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                products.isEmpty() -> {
                    Text(
                        text = "Empty Products",
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                else -> {
                    LazyVerticalGrid(
                        columns = androidx.compose.foundation.lazy.grid.GridCells.Fixed(2),
                        modifier = Modifier.padding(vertical = 8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(products) { item ->
                            ProductItem(product = item, onProductClick = {
                                navController.navigate(Routers.EachProductDetailsScreen(productId = item.productId).route)
                            })
                        }
                    }
                }
            }
        }

    }
}