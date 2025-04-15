package com.nrin31266.shoppingapp.presentation.screen

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.nrin31266.shoppingapp.R
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CheckoutScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController,
    productId: String
) {
    val state = viewModel.getProductByIdState.collectAsStateWithLifecycle()
    val profileState = viewModel.profileScreenState.collectAsStateWithLifecycle()
    val productData = state.value.product

    val email = remember { mutableStateOf("") }
//    val name : String = "",
//    val phoneNumber : String = "",
//    val postalCode : String = "",
//    val province : String = "",
//    val district : String = "",
//    val ward : String = "",
//    val street : String = ""
    val name = remember { mutableStateOf("") }
    val phoneNumber = remember { mutableStateOf("") }
    val postalCode = remember { mutableStateOf("") }
    val province = remember { mutableStateOf("") }
    val district = remember { mutableStateOf("") }
    val ward = remember { mutableStateOf("") }
    val street = remember { mutableStateOf("") }
    val selectedMethod = remember { mutableStateOf("Cash on delivery") }

    LaunchedEffect(key1 = Unit) {
        viewModel.getProductById(productId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Checkout") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                })

        }
    ) { innerPadding ->
        when {
            state.value.isLoading -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            }

            state.value.errorMessage != null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = state.value.errorMessage!!)
                }
            }

            state.value.product == null -> {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(text = "Product not found")
                }
            }

            else -> {
                Column(
                    modifier = Modifier
                        .padding(innerPadding)
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        AsyncImage(
                            model = state.value.product?.image,
                            contentDescription = null,
                            modifier = Modifier
                                .size(80.dp)
                                .border(1.dp, Color.Gray)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text(
                                text = state.value.product!!.name,
                                style = MaterialTheme.typography.bodyLarge
                            )
                            Text(
                                text = "$${state.value.product?.sellingPrice}",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.Bold
                            )
                        }

                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Contact Information", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    OutlinedTextField(
                        value = profileState.value.userData?.userData?.email!!,
                        onValueChange = { },
                        label = { Text("Email") }, enabled = false

                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text("Shipping Address", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        Column(modifier = Modifier.weight(1f)) {
                            OutlinedTextField(
                                value = name.value,
                                onValueChange = { name.value = it },
                                label = { Text("Full Name") }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            OutlinedTextField(modifier = Modifier.weight(1f),
                                value = phoneNumber.value,
                                onValueChange = { phoneNumber.value = it },
                                label = { Text("Phone Number") }
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedTextField(
                        value = province.value,
                        onValueChange = { province.value = it },
                        label = { Text("Province") }
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        OutlinedTextField(modifier = Modifier.weight(1f),
                            value = district.value,
                            onValueChange = { district.value = it },
                            label = { Text("District") }

                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(modifier = Modifier.weight(1f),
                            value = ward.value,
                            onValueChange = { ward.value = it },
                            label = { Text("Ward") }
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Row {
                        OutlinedTextField(
                            modifier = Modifier.weight(1f),
                            value = street.value,
                            onValueChange = { street.value = it },
                            label = { Text("Street")}
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        OutlinedTextField(modifier = Modifier.weight(1f),
                            value = postalCode.value,
                            onValueChange = { postalCode.value = it },
                            label = { Text("Postal Code") }
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text("Payment Method", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row (
                        verticalAlignment = Alignment.CenterVertically
                    ){ 
                        RadioButton(
                            selected = selectedMethod.value == "Cash on delivery",
                            onClick = { selectedMethod.value = "Cash on delivery" }
                        )
                        Text(
                            text = "Cash on delivery",
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Button(
                        onClick = {}, modifier = Modifier.fillMaxWidth(),
                        colors = androidx.compose.material.ButtonDefaults.buttonColors(colorResource(id = R.color.elegant_gold)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text("Continue to Payment")
                    }
                }
            }
        }
    }


}