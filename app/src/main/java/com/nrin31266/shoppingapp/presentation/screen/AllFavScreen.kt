package com.nrin31266.shoppingapp.presentation.screen

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.runtime.key
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.shoppingapp.R
import com.nrin31266.shoppingapp.domain.model.ProductDataModel
import com.nrin31266.shoppingapp.presentation.navigate.Routers
import com.nrin31266.shoppingapp.presentation.utils.FullScreenLoading
import com.nrin31266.shoppingapp.presentation.utils.ProductItem
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AllFavScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.getAllFavouritesState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    val favourites = state.value.products?: emptyList()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
    LaunchedEffect(key1=Unit ) {
        viewModel.getAllFavourites()
    }
    Scaffold (
        topBar = {
            TopAppBar(
                title = { Text("All Favourites",
                    style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold) }
                , scrollBehavior = scrollBehavior,
            )
        }, modifier = Modifier.fillMaxSize().nestedScroll(scrollBehavior.nestedScrollConnection)
    ){innerPadding->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            OutlinedTextField(
                value = "",
                onValueChange = {},
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                shape = RoundedCornerShape(8.dp),
                placeholder = { Text("Search") },
                leadingIcon = { Icon(Icons.Default.Search, "") }
            )

            when{
                state.value.isLoading->{
                    FullScreenLoading()
                }
                state.value.errorMessage!=null->{
                    Text(
                        text = state.value.errorMessage.toString(),
                        color = Color.Red,
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                    )
                }
                favourites.isEmpty() ->{
                    Text(
                        text = state.value.errorMessage.toString(),
                        color = colorResource(id = R.color.purple_700),
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth(),
                    )
                }
                else->{
                    LazyVerticalGrid (
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(15.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ){
                        items(favourites){
                                item ->
                            FavCard (product = item, onFavClick = {
                                navController.navigate(Routers.EachProductDetailsScreen(productId = item.productId).route)
                            })
                        }
                    }
                }

            }

        }
    }
}

@Composable
fun FavCard(
    product: ProductDataModel,
    onFavClick: () -> Unit
){
    Card (
        modifier = Modifier.fillMaxWidth().clickable { onFavClick() }
    ){
        Column (

        ){
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp).height(200.dp), contentScale = ContentScale.Crop
            )

            Column (
                modifier = Modifier.padding(8.dp)

            ){
                Text(text = product.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold
                , color = MaterialTheme.colorScheme.primary,
                    maxLines = 1, overflow = TextOverflow.Ellipsis)
            }
        }
    }
}