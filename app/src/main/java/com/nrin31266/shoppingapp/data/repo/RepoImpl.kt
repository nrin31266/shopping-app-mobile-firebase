package com.nrin31266.shoppingapp.data.repo

import android.content.ContentValues.TAG
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import com.google.firebase.storage.FirebaseStorage
import com.nrin31266.shoppingapp.common.ADD_TO_CART
import com.nrin31266.shoppingapp.common.ADD_TO_FAVOURITES
import com.nrin31266.shoppingapp.common.BANNER_COLLECTION
import com.nrin31266.shoppingapp.common.CATEGORY_COLLECTION
import com.nrin31266.shoppingapp.common.PRODUCT_COLLECTION
import com.nrin31266.shoppingapp.common.ResultState
import com.nrin31266.shoppingapp.common.USER_COLLECTION
import com.nrin31266.shoppingapp.domain.model.BannerDataModel
import com.nrin31266.shoppingapp.domain.model.CartDataModel
import com.nrin31266.shoppingapp.domain.model.CategoryDataModel
import com.nrin31266.shoppingapp.domain.model.ProductDataModel
import com.nrin31266.shoppingapp.domain.model.UserDataModel
import com.nrin31266.shoppingapp.domain.model.UserDataParent
import com.nrin31266.shoppingapp.domain.repo.Repo
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class RepoImpl @Inject constructor(
    val firestore: FirebaseFirestore,
    val auth: FirebaseAuth,
    val storage: FirebaseStorage
) : Repo {
    override fun signupUser(userDataModel: UserDataModel): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)

        try {
            val authResult = auth.createUserWithEmailAndPassword(
                userDataModel.email,
                userDataModel.password
            ).await()

            val uid = authResult.user?.uid ?: throw Exception("User ID is null")

            firestore.collection(USER_COLLECTION)
                .document(uid)
                .set(userDataModel)
                .await()

            emit(ResultState.Success("User registered successfully and added to database!"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "Registration failed"))
        }
    }.flowOn(Dispatchers.IO)


    override fun loginUser(email: String, password: String): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            val authResult = auth.signInWithEmailAndPassword(email, password).await()
            val uid = authResult.user?.uid ?: throw Exception("User ID is null")
            emit(ResultState.Success("User logged in successfully"))
        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "Login failed"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getUserDataById(userId: String): Flow<ResultState<UserDataParent>> = flow {
        emit(ResultState.Loading)
        try {
            val firestoreResult = firestore.collection(USER_COLLECTION).document(userId).get().await()
            val userData = firestoreResult.toObject(UserDataModel::class.java)
            if (userData != null) {
                emit(ResultState.Success(UserDataParent(firestoreResult.id, userData)))
            } else {
                emit(ResultState.Error("User data not found"))
            }
        }catch (e : Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }

    }.flowOn(Dispatchers.IO)

    override fun updateUserData(userDataParent: UserDataParent): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try{
            firestore.collection(USER_COLLECTION).document(userDataParent.nodeId).update(userDataParent.userData.toMap()).await()
            Log.d(TAG, "updateUserData: $userDataParent")
            emit(ResultState.Success("User data updated successfully"))
        }catch (e: Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }

    }.flowOn(Dispatchers.IO)

    override fun userProfileImage(uri: Uri): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)

        try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val storageRef = storage.reference.child("profile_images/${currentUser.uid}" + "_" + UUID.randomUUID())
            val uploadTask = storageRef.putFile(uri).await()
            val downloadUrl = uploadTask.storage.downloadUrl.await()

            emit(ResultState.Success(downloadUrl.toString()))
        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "Failed to upload image"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getCategoriesInLimited(): Flow<ResultState<List<CategoryDataModel>>> = flow {
        emit(ResultState.Loading)
        try {
            val snapshot = firestore.collection(CATEGORY_COLLECTION)
                .limit(7)
                .get()
                .await()
            val categories = snapshot.toObjects(CategoryDataModel::class.java)
            emit(ResultState.Success(categories))
        } catch (e: Exception) {
            emit(ResultState.Error(e.localizedMessage ?: "Failed to get categories"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getProductsInLimited(): Flow<ResultState<List<ProductDataModel>>> = flow {
        emit(ResultState.Loading)
        try {
            val snapshot = firestore.collection(PRODUCT_COLLECTION)
                .limit(7)
                .get()
                .await()
            val products = snapshot.toObjects(ProductDataModel::class.java)
            emit(ResultState.Success(products))
        }catch (e: Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Failed to get products"))
        }

    }.flowOn(Dispatchers.IO)

    override fun getAllProducts(): Flow<ResultState<List<ProductDataModel>>> = flow {
        emit(ResultState.Loading)
        try {
            val snapshot = firestore.collection(PRODUCT_COLLECTION)
                .get()
                .await()
            val products = snapshot.toObjects(ProductDataModel::class.java)
            emit(ResultState.Success(products))
        }catch (e: Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Failed to get products"))
        }

    }.flowOn(Dispatchers.IO)

    override fun getProductById(productId: String): Flow<ResultState<ProductDataModel>> = flow {
        emit(ResultState.Loading)
        try {
            val snapshot = firestore.collection(PRODUCT_COLLECTION).document(productId).get().await()
            val product = snapshot.toObject(ProductDataModel::class.java)
            product ?: throw Exception("Product not found")
        }catch (e:Exception){
            emit(ResultState.Error(e.localizedMessage?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun addToCart(cartDataModel: CartDataModel): Flow<ResultState<String>> = flow {
        emit(ResultState.Loading)
        try {
            firestore.collection(ADD_TO_CART).document(auth.currentUser!!.uid).collection("user_cart")
                .add(cartDataModel).await()
            emit(ResultState.Success("Product added to cart"))
        }catch (e: Exception){
            emit(ResultState.Error(e.localizedMessage?: "Unknown error"))
        }
    }

    override fun addToFavourites(productDataModel: ProductDataModel): Flow<ResultState<String>> = flow<ResultState<String>> {
        emit(ResultState.Loading)
        try {
            firestore.collection(ADD_TO_FAVOURITES).document(auth.currentUser!!.uid).collection("user_fav").add(productDataModel).await()
            emit(ResultState.Success("Product added to favourites"))
        }catch (e:Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }

    }.flowOn(Dispatchers.IO)

    override fun getAllFavourites(): Flow<ResultState<List<ProductDataModel>>> = flow<ResultState<List<ProductDataModel>>> {
        emit(ResultState.Loading)
        try {
            val snapshot = firestore.collection(ADD_TO_FAVOURITES).document(auth.currentUser!!.uid).collection("user_fav").get().await()
            emit(ResultState.Success(snapshot.toObjects(ProductDataModel::class.java)))
        }catch (e:Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getCart(): Flow<ResultState<List<CartDataModel>>> = flow<ResultState<List<CartDataModel>>> {
        emit(ResultState.Loading)
        try {
            val snapshot = firestore.collection(ADD_TO_CART).document(auth.currentUser!!.uid).collection("user_cart").get().await()
            emit(ResultState.Success(snapshot.toObjects(CartDataModel::class.java)))
        }catch (e:Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getAllCategories(): Flow<ResultState<List<CategoryDataModel>>> = flow {
        emit(ResultState.Loading)
        try {
            val snapshot = firestore.collection(CATEGORY_COLLECTION).get().await()
            emit(ResultState.Success(snapshot.toObjects(CategoryDataModel::class.java)))
        }catch (e:Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }
    }

    override fun getCheckout(productId: String): Flow<ResultState<ProductDataModel>> = flow {
        emit(ResultState.Loading)
        try {

            val snapshot= firestore.collection(PRODUCT_COLLECTION).document(productId).get().await()
            val product = snapshot.toObject(ProductDataModel::class.java)
            emit(ResultState.Success(product!!))

        }catch (e:Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)

    override fun getBanners(): Flow<ResultState<List<BannerDataModel>>> = flow<ResultState<List<BannerDataModel>>> {
        emit(ResultState.Loading)
        try {
            val snapshot  = firestore.collection(BANNER_COLLECTION).get().await()
            emit(ResultState.Success(snapshot.toObjects(BannerDataModel::class.java)))
        }catch (e:Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }

    }.flowOn(Dispatchers.IO)

    override fun getSpecificCategoryItem(categoryName: String): Flow<ResultState<List<ProductDataModel>>> = flow {
        emit(ResultState.Loading)
       try {
           val snapshot = firestore.collection(PRODUCT_COLLECTION).whereEqualTo("category", categoryName).get().await()
           val products = snapshot.toObjects(ProductDataModel::class.java)
           emit(ResultState.Success(products))
       }catch (e:Exception){
           emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
       }

    }.flowOn(Dispatchers.IO)

    override fun getAllSuggestedProducts(): Flow<ResultState<List<ProductDataModel>>> = flow<ResultState<List<ProductDataModel>>> {
        emit(ResultState.Loading)
        try {
            val snapshot = firestore.collection(ADD_TO_FAVOURITES).document(auth.currentUser!!.uid)
                .collection("user_fav").get().await()
            val products = snapshot.toObjects(ProductDataModel::class.java)
            emit(ResultState.Success(products))
        }catch (e:Exception){
            emit(ResultState.Error(e.localizedMessage ?: "Unknown error"))
        }
    }.flowOn(Dispatchers.IO)
}