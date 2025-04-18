package com.nrin31266.shoppingapp.presentation.screen

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContract
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ButtonDefaults

import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button


import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Label
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextFieldDefaults

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import coil.compose.AsyncImagePainter
import coil.compose.SubcomposeAsyncImage
import coil.compose.SubcomposeAsyncImageContent
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import com.google.firebase.auth.FirebaseAuth
import com.nrin31266.shoppingapp.R
import com.nrin31266.shoppingapp.domain.model.UserDataModel
import com.nrin31266.shoppingapp.domain.model.UserDataParent
import com.nrin31266.shoppingapp.presentation.navigate.Routers
import com.nrin31266.shoppingapp.presentation.utils.LogoutAlertDialog
import com.nrin31266.shoppingapp.presentation.viewmodel.ShoppingAppViewmodel

@Composable
fun ProfileScreen(
    viewModel: ShoppingAppViewmodel = hiltViewModel(),
    navController: NavController,
    firebaseAuth: FirebaseAuth
) {
    LaunchedEffect(key1 = Unit) {
        viewModel.getUserDataById(firebaseAuth.currentUser?.uid!!)
    }

    val profileScreenState = viewModel.profileScreenState.collectAsStateWithLifecycle()
    val updateScreenState = viewModel.updateScreenState.collectAsStateWithLifecycle()
    val uploadUserProfileImageState =
        viewModel.uploadUserProfileImageState.collectAsStateWithLifecycle()

    val context = LocalContext.current
    var showDialog = remember { mutableStateOf(false) }
    var isEditing = uploadUserProfileImageState.value.isLoading || updateScreenState.value.isLoading

    var imageUri = remember { mutableStateOf<Uri?>(null) }
    var profileImage = remember { mutableStateOf("") }

    var fullName = remember { mutableStateOf("") }
    var email = remember { mutableStateOf("") }
    var phoneNumber = remember { mutableStateOf("") }

    LaunchedEffect(profileScreenState.value.userData) {
        profileScreenState.value.userData?.userData.let {
            fullName.value = it?.fullName ?: ""
            email.value = it?.email ?: ""
            phoneNumber.value = it?.phoneNumber ?: ""
            profileImage.value = it?.profileImage ?: ""


        }
    }
    LaunchedEffect(uploadUserProfileImageState.value.imageUrl) {
        uploadUserProfileImageState.value.imageUrl?.let {
            profileImage.value = it
            Log.d("ádafasfasf", "ProfileScreen: ${uploadUserProfileImageState.value.imageUrl}")
        }
    }


    val pickMedia =
        rememberLauncherForActivityResult(contract = ActivityResultContracts.PickVisualMedia()) {
            if (it != null) {
                viewModel.uploadUserProfileImage(it)
                imageUri.value = it
            }

        }

    if (updateScreenState.value.userData != null) {
        Toast.makeText(context, updateScreenState.value.userData, Toast.LENGTH_SHORT).show()
    } else if (updateScreenState.value.errorMessage != null) {
        Toast.makeText(context, updateScreenState.value.errorMessage, Toast.LENGTH_SHORT).show()
    } else if (updateScreenState.value.isLoading) {
//        Box(
//            modifier = Modifier.fillMaxSize()
//        ) {
//            CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
//        }

    }

//    if (uploadUserProfileImageState.value.imageUrl != null) {
//        profileImage.value = uploadUserProfileImageState.value.imageUrl.toString()
//    } else
    if (uploadUserProfileImageState.value.errorMessage != null) {
        Toast.makeText(context, uploadUserProfileImageState.value.errorMessage, Toast.LENGTH_SHORT)
            .show()
    } else if (uploadUserProfileImageState.value.isLoading) {

    }
    if (profileScreenState.value.userData != null) {
        Scaffold(
            modifier = Modifier.fillMaxSize()

        ) { innerPadding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .padding(horizontal = 10.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .align(Alignment.Start)
                        .clickable {
                            pickMedia.launch(
                                PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly)
                            )
                        }.clip(CircleShape)
                    , contentAlignment = Alignment.Center

                ) {
                    if(imageUri.value != null || profileImage.value.isNotEmpty()){
                        Image(
                            painter = rememberAsyncImagePainter(
                                model = imageUri.value ?: profileImage.value
                            ),
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),
                            contentScale = ContentScale.Crop
                        )
                    }else{
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier
                                .size(120.dp)
                                .clip(CircleShape)
                                .background(Color.LightGray),

                        )
                    }

                    if (uploadUserProfileImageState.value.isLoading) {
                        Box(
                            modifier = Modifier
                                .matchParentSize()
                                .background(Color(0x88000000)) // mờ overlay
                        ) {
                            CircularProgressIndicator(
                                modifier = Modifier.align(Alignment.Center),
                                color = colorResource(id = R.color.elegant_gold)
                            )
                        }
                    }
                }

                Spacer(Modifier.height(10.dp))
                OutlinedTextField(
                    value = email.value,
                    onValueChange = { email.value = it },
                    label = { Text("Email") },
                    colors = OutlinedTextFieldDefaults.colors(
                        unfocusedBorderColor = Color.Gray,
                        focusedBorderColor = colorResource(id = R.color.elegant_gold)
                    ),
                    readOnly = true,
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(8.dp))
                Row {
                    OutlinedTextField(
                        value = fullName.value,
                        onValueChange = { fullName.value = it },

                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Gray,
                            focusedBorderColor = colorResource(id = R.color.elegant_gold)
                        ),
                        label = { Text("Full Name") },
                        readOnly = isEditing

                    )

                    Spacer(modifier = Modifier.size(16.dp))

                    OutlinedTextField(
                        value = phoneNumber.value,
                        onValueChange = { phoneNumber.value = it },
                        label = { Text("Phone") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            unfocusedBorderColor = Color.Gray,
                            focusedBorderColor = colorResource(id = R.color.elegant_gold)
                        ),
                        shape = RoundedCornerShape(10.dp),
                        readOnly = isEditing
                    )
                }


                Spacer(modifier = Modifier.size(16.dp))
                Column(modifier = Modifier.fillMaxWidth()) {
                    Button(
                        onClick = {
                            // Save changes
                            val updateUserData = UserDataModel(
                                fullName = fullName.value,
                                email = email.value,
                                phoneNumber = phoneNumber.value,
                                profileImage = profileImage.value
                            )

                            viewModel.updateUserData(
                                UserDataParent(
                                    nodeId = profileScreenState.value.userData?.nodeId!!,
                                    updateUserData
                                )
                            )


                        },
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(colorResource(id = R.color.elegant_gold)),
                        modifier = Modifier.fillMaxWidth(),
                        enabled = !isEditing
                    ) {
                        Text(if (isEditing) "Savinggggg" else "Edit")
                    }


                    Spacer(Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = {
                            showDialog.value = true
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(10.dp),
                        enabled = !isEditing
                    ) {

                        Text("Log out")
                    }

                    if (showDialog.value) {
                        LogoutAlertDialog(
                            onDismiss = { showDialog.value = false },
                            onConfirm = {
                                showDialog.value = false
                                firebaseAuth.signOut()
                                navController.navigate(Routers.LoginScreen.route)
                            }
                        )
                    }
                }
            }
        }
    }

}