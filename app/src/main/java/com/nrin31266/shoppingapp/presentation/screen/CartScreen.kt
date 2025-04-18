package com.nrin31266.shoppingapp.presentation.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.rememberScrollableState
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.shoppingapp.R
import com.nrin31266.shoppingapp.domain.model.CartDataModel
import com.nrin31266.shoppingapp.presentation.utils.BackButton
import com.nrin31266.shoppingapp.presentation.utils.FullScreenLoading
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController
) {
    val cartState = viewModel.getCartState.collectAsStateWithLifecycle()
    val cartData = cartState.value.cart ?: emptyList()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())


    LaunchedEffect(key1 = Unit) {
        viewModel.getCart()
    }

    Scaffold(
        modifier = Modifier
            .fillMaxSize(),

        bottomBar = {
            Column {
                HorizontalDivider(
                    modifier = Modifier.padding(vertical = 8.dp)
                )
                Row(
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .fillMaxWidth()
                ) {
                    Button(
                        modifier = Modifier.weight(1f),
                        onClick = { /*TODO*/ },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(R.color.elegant_gold))

                    ) {
                        Text(
                            "Checkout",
                            style = MaterialTheme.typography.bodyMedium,
                        )
                    }

                }
            }
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            when {
                cartState.value.isLoading -> {
                    FullScreenLoading()
                }

                cartState.value.errorMessage != null -> {
                    Text(
                        text = cartState.value.errorMessage.toString(),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                cartData.isEmpty() -> {
                    Text(
                        text = "Your cart is empty",
                        color = colorResource(R.color.dark_slate),
                        style = MaterialTheme.typography.bodyMedium,
                    )
                }

                else -> {
                    Row(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            "Items",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.elegant_gold)
                        )
                        Spacer(modifier = Modifier.weight(.45f))
                        Text(
                            "Details",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.elegant_gold)
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            "Quantity",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Bold,
                            color = colorResource(id = R.color.elegant_gold)

                        )

                    }
                    LazyColumn(
                        modifier = Modifier.weight(0.6f).padding(horizontal = 8.dp)
                    ) {
                        items(cartData) { item ->
                            CartItemCard(cartData = item)
                        }
                    }

                }

            }

        }
    }
}

@Composable
fun CartItemCard(cartData: CartDataModel) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            ,
        shape = RoundedCornerShape(8.dp),


    ) {
        Row(
            modifier = Modifier.padding(6.dp), verticalAlignment = Alignment.CenterVertically
        ) {

            AsyncImage(
                model = cartData.image,
                contentDescription = null,
                modifier = Modifier
                    .height(80.dp)
                    .width(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop

            )
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 16.dp)
            ) {
                Text(
                    cartData.name, style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 2, overflow = TextOverflow.Ellipsis
                )

                Text(
                    if (cartData.size.isNotEmpty()) "Size: ${cartData.size}" else "Q",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.secondary
                )
                Text(
                    cartData.price,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Column {
                Text(
                    "x${cartData.quantity}",
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.primary
                )
            }

        }
    }
}