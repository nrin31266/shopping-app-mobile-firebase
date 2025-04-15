package com.nrin31266.shoppingapp.presentation.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.shoppingapp.R
import com.nrin31266.shoppingapp.common.ResultState
import com.nrin31266.shoppingapp.domain.model.CartDataModel
import com.nrin31266.shoppingapp.presentation.navigate.Routers
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailsScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController,
    productId: String
) {
    val getProductByIdState = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    val context = LocalContext.current
    var selectedSize by remember {
        mutableStateOf("")
    }

    var quantity by remember {
        mutableIntStateOf(1)
    }

    var isFavorite by remember { mutableStateOf(false) }

    LaunchedEffect(key1 = Unit) {
        viewModel.getProductById(productId)

    }

    Scaffold(
        modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection)
        , topBar = {
            TopAppBar(
                title = {
                    Text(text = "Product Details", )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton (onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) {
        innerPadding ->

        Column (
            modifier = Modifier.padding(innerPadding).fillMaxSize().verticalScroll(
                rememberScrollState()
            )
        ){
            when {
                getProductByIdState.value.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ){
                        CircularProgressIndicator()
                    }

                }
                getProductByIdState.value.errorMessage != null -> {

                    Box(
                        modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center
                    ){
                        Text(text = getProductByIdState.value.errorMessage!!)
                    }
                }
                getProductByIdState.value.product != null -> {

                    val product = getProductByIdState.value.product!!

                    Box(modifier = Modifier.height(300.dp)){
                        AsyncImage(
                            model = product.image,
                            contentDescription = null
                            , modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Column (
                        modifier = Modifier.padding(16.dp)
                    ){
                        Text(text = product.name,
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold)
                        Spacer(modifier = Modifier.height(8.dp))

                        Text(text = "$${product.sellingPrice}",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Size",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold)
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        , modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ){
                            listOf("S", "M", "L", "XL").forEach{
                                    size->
                                    OutlinedButton (
                                        onClick = { selectedSize = size },
                                        colors = ButtonDefaults.buttonColors(
                                            if (selectedSize == size) MaterialTheme.colorScheme.primary else MaterialTheme
                                                .colorScheme.surface
                                        ),
                                        shape = MaterialTheme.shapes.small,
                                        modifier = Modifier.weight(1f)
                                    ) {
                                        Text(size)
                                    }
                            }
                        }


                    }

                    Text("Quantity", style = MaterialTheme.typography.labelLarge
                    , modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))

                    Row (
                        verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ){
                        IconButton({
                            if(quantity>1) {quantity -=1}
                        }) {
                            Text("-", style = MaterialTheme.typography.headlineMedium)

                        }
                        Text("$quantity", style = MaterialTheme.typography.headlineMedium)

                        IconButton({
                            quantity +=1
                        }) {
                            Text("+", style = MaterialTheme.typography.headlineMedium)
                        }
                        Text("|")
                        Button(
                            {
                                val cartData = CartDataModel(
                                    name = product.name,
                                    image = product.image,
                                    price = product.sellingPrice,
                                    quantity = quantity,
                                    size = selectedSize,
                                    productId = product.productId,
                                    description = product.description,
                                    category = product.category
                                )

                                viewModel.addToCart(
                                    cartData,
                                )

                            }, modifier = Modifier.weight(1f), colors = ButtonDefaults.buttonColors(
                                colorResource(id = R.color.elegant_gold)
                            )
                        ) {
                            Icon(Icons.Default.ShoppingCart, "")
                        }
                    }
                    Row  (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)){
                        OutlinedButton(
                            {
                                isFavorite =!isFavorite
                                viewModel.addToFavourites(product)
                            },
                        ) {
                            Icon(if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,"", )
                        }
                        Button(
                            {
                                navController.navigate(Routers.CheckoutScreen(productId=product.productId).route)
                            }, modifier = Modifier.weight(1f)
                        ) {
                            Text("Buy now")
                        }

                    }


                    Text("Description", style = MaterialTheme.typography.labelLarge
                        , modifier = Modifier.padding(top = 16.dp, bottom = 8.dp))
                    Text(product.description, style = MaterialTheme.typography.bodyMedium)




                }

                else -> {

                }
            }
        }


    }


}