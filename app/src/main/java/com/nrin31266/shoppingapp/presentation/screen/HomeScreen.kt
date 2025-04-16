package com.nrin31266.shoppingapp.presentation.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nrin31266.shoppingapp.domain.model.CategoryDataModel
import com.nrin31266.shoppingapp.domain.model.ProductDataModel
import com.nrin31266.shoppingapp.presentation.navigate.Routers
import com.nrin31266.shoppingapp.presentation.utils.FullScreenLoading
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@Composable
fun HomeScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController
) {
    val state = viewModel.homeScreenState.collectAsStateWithLifecycle()
    val getSuggestedProductsState =
        viewModel.getSuggestedProductsState.collectAsStateWithLifecycle()
    val suggestedProducts = getSuggestedProductsState.value.products ?: emptyList()
    val context = LocalContext.current
    LaunchedEffect(key1 = Unit) {
        viewModel.getAllSuggestedProducts()
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
    ) { innerPadding ->
        if (state.value.isLoading) {
            FullScreenLoading()
        }
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
        ) {
            if (state.value.errorMessage != null) {
                Text(
                    text = state.value.errorMessage.toString(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Column(

                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)

                    ) {
                        OutlinedTextField(
                            value = "",
                            onValueChange = {},
                            modifier = Modifier.weight(1f).padding(8.dp),
                            shape = RoundedCornerShape(8.dp),
                            placeholder = { Text("Search") },
                            leadingIcon = { Icon(Icons.Default.Search, "") },

                            )
                        Spacer(modifier = Modifier.size(8.dp))
                        IconButton(
                            onClick = {

                            }

                        ) {
                            Icon(Icons.Default.Notifications, "",
                                modifier = Modifier.size(30.dp))
                        }

                    }
                    Row (
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp).fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ){
                        Text(
                            "Categories",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )
                        TextButton(
                            {
                                navController.navigate(Routers.AllCategoriesScreen.route)
                            }
                        ) {
                            Text(
                                "See All",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                    Row (
                        modifier = Modifier.padding(vertical = 8.dp, horizontal = 12.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ){

                    }
                }

            }

        }

    }


}

@Composable
fun CategoryItem(
    category: CategoryDataModel,
    onCategoryClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable {
            onCategoryClick()
        }
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(Color.LightGray, CircleShape)
        ) {
            AsyncImage(
                model = category.image,
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = category.name,
                modifier = Modifier
                    .align(Alignment.Center)
                    .fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.bodyMedium

            )
        }
    }
}

@Composable
fun FlashSale(
    product: ProductDataModel,
    navController: NavController

) {
    Card(
        modifier = Modifier
            .width(150.dp)
            .clickable {
                navController.navigate(Routers.EachProductDetailsScreen(productId = product.productId).route)
            }
            .aspectRatio(0.7f),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(

        ) {
            AsyncImage(
                model = product.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .height(150.dp)
                    .width(100.dp)
                    .clip(RoundedCornerShape(10.dp)),
                contentScale = ContentScale.Crop
            )
            Column(
                modifier = Modifier.padding(8.dp)
            ) {
                Text(
                    text = product.name,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = "$${product.sellingPrice}",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.size(12.dp))
                    Text(
                        text = "${product.availableUnits} left",
                        style = MaterialTheme.typography.bodyMedium,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.Gray
                    )
                }
            }
        }
    }
}


fun importCategoriesFromAssets(context: Context) {
    val firestore = Firebase.firestore
    val json = context.assets.open("categories.json").bufferedReader().use { it.readText() }

    val listType = object : TypeToken<List<CategoryDataModel>>() {}.type
    val categories: List<CategoryDataModel> = Gson().fromJson(json, listType)

    categories.forEach { category ->
        firestore.collection("categories")
            .add(category)
            .addOnSuccessListener {
                Log.d("Firestore", "Added: ${category.name}")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Failed: ${it.message}")
            }
    }
}

fun importProductsFromAssets(context: Context) {
    val firestore = Firebase.firestore
    val json = context.assets.open("products.json").bufferedReader().use { it.readText() }

    val listType = object : TypeToken<List<ProductDataModel>>() {}.type
    val products: List<ProductDataModel> = Gson().fromJson(json, listType)

    products.forEach { product ->
        // Tạo sản phẩm với productId là null
        val productWithId = product.copy(productId = "")

        firestore.collection("products")
            .add(productWithId)
            .addOnSuccessListener { documentReference ->
                // Sau khi Firebase tạo documentId, cập nhật lại productId
                val updatedProduct = product.copy(productId = documentReference.id)

                // Cập nhật lại productId trong Firestore
                firestore.collection("products")
                    .document(documentReference.id)
                    .set(updatedProduct)
                    .addOnSuccessListener {
                        Log.d("Firestore", "Updated product with ID: ${updatedProduct.productId}")
                    }
                    .addOnFailureListener {
                        Log.e("Firestore", "Failed to update product ID: ${it.message}")
                    }
            }
            .addOnFailureListener {
                Log.e("Firestore", "Failed to add product: ${it.message}")
            }
    }
}
