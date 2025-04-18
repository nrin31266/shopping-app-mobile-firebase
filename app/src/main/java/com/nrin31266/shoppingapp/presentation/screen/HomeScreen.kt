package com.nrin31266.shoppingapp.presentation.screen

import android.content.Context
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.nrin31266.shoppingapp.common.HomeScreenState
import com.nrin31266.shoppingapp.domain.model.BannerDataModel
import com.nrin31266.shoppingapp.domain.model.CategoryDataModel
import com.nrin31266.shoppingapp.domain.model.ProductDataModel
import com.nrin31266.shoppingapp.presentation.navigate.Routers
import com.nrin31266.shoppingapp.presentation.utils.Banner
import com.nrin31266.shoppingapp.presentation.utils.FullScreenLoading
import com.nrin31266.shoppingapp.presentation.utils.ProductItem
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
//    LaunchedEffect(key1 = suggestedProducts) {
//        if (suggestedProducts.isEmpty()) {
//            viewModel.getAllSuggestedProducts()
//        }
//    }


    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            SearchBar()
        },
        contentWindowInsets = WindowInsets(0)
    ) { innerPadding ->
        if (state.value.isLoading) {
            FullScreenLoading()
        }

        HomeContent(
            state = state.value,
            suggestedProducts = suggestedProducts,
            navController = navController,
            innerPadding = innerPadding
        )
    }
}

@Composable
fun SearchBar() {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp)
    ) {
        OutlinedTextField(
            value = "",
            onValueChange = {},
            modifier = Modifier.weight(1f),
            shape = RoundedCornerShape(8.dp),
            placeholder = { Text(text = "Search...", fontSize = 13.sp) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null) },
            textStyle = TextStyle(fontSize = 14.sp),
            singleLine = true
        )
        Spacer(modifier = Modifier.size(8.dp))
        IconButton(onClick = {}) {
            Icon(Icons.Default.Notifications, "", modifier = Modifier.size(30.dp))
        }
    }
}

@Composable
fun HomeContent(
    state: HomeScreenState,
    suggestedProducts: List<ProductDataModel>,
    navController: NavController,
    innerPadding: PaddingValues
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
            .verticalScroll(rememberScrollState())
    ) {
        if (state.errorMessage != null) {
            Text(
                text = state.errorMessage.toString(),
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.error
            )
        } else {
            CategorySection(state.categories, navController)
            Banner(state.banners)
            FlashSaleSection(state.products, navController)
            SuggestedProductsSection(suggestedProducts, navController)
        }
    }
}

@Composable
fun CategorySection(categories: List<CategoryDataModel>, navController: NavController) {
    Row(
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Categories",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TextButton({ navController.navigate(Routers.AllCategoriesScreen.route) }) {
            Text(
                "See All",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(categories) { category ->
            CategoryItem(category = category) {
                navController.navigate(Routers.ProductsInCategoryScreen(categoryName = category.name).route)
            }
        }
    }
}


@Composable
fun FlashSaleSection(products: List<ProductDataModel>, navController: NavController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Flash Sale",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TextButton({ navController.navigate(Routers.SeeAllProductScreen.route) }) {
            Text(
                "See All",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(products) { product ->
            FlashSale(product = product, navController = navController)
        }
    }
}

@Composable
fun SuggestedProductsSection(
    suggestedProducts: List<ProductDataModel>,
    navController: NavController
) {
    // Title and See All button
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            "Suggested for you",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        TextButton({ navController.navigate(Routers.SeeAllProductScreen.route) }) {
            Text(
                "See All",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(suggestedProducts) { product ->
            FlashSale(product = product, navController = navController)
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
        modifier = Modifier
            .clickable { onCategoryClick() }
            .padding(2.dp) // thêm tí không gian
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.LightGray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = category.image,
                contentDescription = null,
                modifier = Modifier
                    .size(63.dp) // nhỏ hơn box để có viền đều
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
        }
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = category.name,
            modifier = Modifier.widthIn(max = 80.dp), // Giới hạn chiều rộng
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Center
        )
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

fun importBannersFromAssets(context: Context) {
    val firestore = Firebase.firestore
    val json = context.assets.open("banners.json").bufferedReader().use { it.readText() }

    val listType = object : TypeToken<List<BannerDataModel>>() {}.type
    val banners: List<BannerDataModel> = Gson().fromJson(json, listType)

    banners.forEach { banner ->
        firestore.collection("banners")
            .add(banner)
            .addOnSuccessListener { documentReference ->
                Log.d("Firestore", "Banner added with ID: ${documentReference.id}")
            }
            .addOnFailureListener {
                Log.e("Firestore", "Failed to add banner: ${it.message}")
            }
    }
}

