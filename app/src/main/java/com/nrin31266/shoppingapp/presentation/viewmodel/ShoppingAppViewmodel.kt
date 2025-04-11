package com.nrin31266.shoppingapp.presentation.viewmodel


import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.shoppingapp.common.HomeScreenState
import com.nrin31266.shoppingapp.common.ResultState
import com.nrin31266.shoppingapp.domain.model.CartDataModel
import com.nrin31266.shoppingapp.domain.model.CategoryDataModel
import com.nrin31266.shoppingapp.domain.model.ProductDataModel
import com.nrin31266.shoppingapp.domain.model.UserDataModel
import com.nrin31266.shoppingapp.domain.model.UserDataParent
import com.nrin31266.shoppingapp.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ShoppingAppViewmodel @Inject constructor(
    private val signupUserUseCase: SignupUserUseCase,
    private val loginUserUseCase: LoginUserUseCase,
    private val getUserDataByIdUseCase: GetUserDataByIdUseCase,
    private val getProductsInLimitUseCase: GetProductsInLimitUseCase,
    private val getAllProductUseCase: GetAllProductUseCase,
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val getCartUseCase: GetCartUseCase,
    private val updateUserDataUseCase: UpdateUserDataUseCase,
    private val userProfileImageUseCase: UserProfileImageUseCase,
    private val getCategoriesInLimitUseCase: GetCategoriesInLimitUseCase,
    private val addToCartUseCase: AddToUserCartUseCase,
    private val addToFavUseCase: AddToFavUseCase,
    private val getAllFavUseCase: GetAllFavUseCase,
    private val getAllCategoryUseCase: GetAllCategoryUseCase,
    private val getCheckoutUseCase: GetCheckoutUseCase,
    private val getBannersUseCase: GetBannersUseCase,
    private val getSpecificCategoryItemsUseCase: GetSpecificCategoryItemsUseCase,
    private val getAllSuggestedProductUseCase: GetAllSuggestedProductUseCase
) : ViewModel() {

    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState: StateFlow<ProfileScreenState> = _profileScreenState.asStateFlow()

    private val _signUpScreenState = MutableStateFlow(SignUpScreenScreenState())
    val signUpScreenState: StateFlow<SignUpScreenScreenState> = _signUpScreenState.asStateFlow()

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState: StateFlow<LoginScreenState> = _loginScreenState.asStateFlow()

    private val _updateScreenState = MutableStateFlow(UpdateScreenState())
    val updateScreenState: StateFlow<UpdateScreenState> = _updateScreenState.asStateFlow()

    private val _uploadUserProfileImageState = MutableStateFlow(UploadUserProfileImageState())
    val uploadUserProfileImageState: StateFlow<UploadUserProfileImageState> =
        _uploadUserProfileImageState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetProductByIdState())
    val getProductByIdState: StateFlow<GetProductByIdState> = _getProductByIdState.asStateFlow()

    private val _addToCartState = MutableStateFlow(AddToCartState())
    val addToCartState: StateFlow<AddToCartState> = _addToCartState.asStateFlow()

    private val _addToFavouritesState = MutableStateFlow(AddToFavouritesState())
    val addToFavouritesState: StateFlow<AddToFavouritesState> = _addToFavouritesState.asStateFlow()

    private val _getAllFavouritesState = MutableStateFlow(GetAllFavouritesState())
    val getAllFavouritesState: StateFlow<GetAllFavouritesState> =
        _getAllFavouritesState.asStateFlow()

    private val _getAllProductsState = MutableStateFlow(GetAllProductsState())
    val getAllProductsState: StateFlow<GetAllProductsState> = _getAllProductsState.asStateFlow()

    private val _getCartState = MutableStateFlow(GetCartState())
    val getCartState: StateFlow<GetCartState> = _getCartState.asStateFlow()

    private val _getAllCategoriesState = MutableStateFlow(GetAllCategoriesState())
    val getAllCategoriesState: StateFlow<GetAllCategoriesState> =
        _getAllCategoriesState.asStateFlow()

    private val _getCheckoutState = MutableStateFlow(GetCheckoutState())
    val getCheckoutState: StateFlow<GetCheckoutState> = _getCheckoutState.asStateFlow()

    private val _getSpecificCategoryItemState = MutableStateFlow(GetSpecificCategoryItemState())
    val getSpecificCategoryItemState: StateFlow<GetSpecificCategoryItemState> =
        _getSpecificCategoryItemState.asStateFlow()

    private val _getSuggestedProductsState = MutableStateFlow(GetSuggestedProductsState())
    val getSuggestedProductsState: StateFlow<GetSuggestedProductsState> =
        _getSuggestedProductsState.asStateFlow()

    private val _homeScreenState = MutableStateFlow(HomeScreenState())
    val homeScreenState: StateFlow<HomeScreenState> = _homeScreenState.asStateFlow()


    init {
        loadHomeScreenData()
    }

    private fun loadHomeScreenData() {
        viewModelScope.launch {
            combine(
                getBannersUseCase.getBanners(),
                getCategoriesInLimitUseCase.getCategoriesInLimited(),
                getProductsInLimitUseCase.getProductsInLimited()
            ) { banners, categories, products ->

                when {
                    banners is ResultState.Error -> {
                        HomeScreenState(
                            errorMessage = banners.message,
                            isLoading = false
                        )
                    }

                    categories is ResultState.Error -> {
                        HomeScreenState(
                            errorMessage = categories.message,
                            isLoading = false
                        )
                    }

                    products is ResultState.Error -> {
                        HomeScreenState(
                            errorMessage = products.message,
                            isLoading = false
                        )
                    }

                    banners is ResultState.Success &&
                            categories is ResultState.Success &&
                            products is ResultState.Success -> {
                        HomeScreenState(
                            banners = banners.data,
                            categories = categories.data,
                            products = products.data,
                            isLoading = false
                        )
                    }

                    else -> {
                        HomeScreenState(isLoading = true)
                    }
                }

            }.collect { state ->
                _homeScreenState.value = state
            }
        }
    }


    fun getSpecificCategoryItems(categoryName: String) {
        viewModelScope.launch {
            getSpecificCategoryItemsUseCase.getSpecificCategoryItems(categoryName).collect {
                when (it) {
                    is ResultState.Success -> {
                        _getSpecificCategoryItemState.value =
                            _getSpecificCategoryItemState.value.copy(
                                products = it.data,
                                isLoading = false
                            )
                    }

                    is ResultState.Error -> {
                        _getSpecificCategoryItemState.value =
                            _getSpecificCategoryItemState.value.copy(
                                errorMessage = it.message,
                                isLoading = false
                            )
                    }

                    is ResultState.Loading -> {
                        _getSpecificCategoryItemState.value =
                            _getSpecificCategoryItemState.value.copy(
                                isLoading = true
                            )
                    }
                }
            }
        }
    }

    fun getCheckout(productId: String) {
        viewModelScope.launch {
            getCheckoutUseCase.getCheckout(productId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            isLoading = true
                        )

                    }

                    is ResultState.Success -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            product = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getCheckoutState.value = _getCheckoutState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }


        }
    }

    fun getAllCategories() {
        viewModelScope.launch {
            getAllCategoryUseCase.getCategories().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            categories = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }

        }

    }

    fun getCart() {
        viewModelScope.launch {
            getCartUseCase.getCart().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getCartState.value = _getCartState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getCartState.value = _getCartState.value.copy(
                            cart = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getCartState.value = _getCartState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun signupUser(userDataModel: UserDataModel) {
        viewModelScope.launch {
            signupUserUseCase.signupUser(userDataModel).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _signUpScreenState.value = _signUpScreenState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _signUpScreenState.value = _signUpScreenState.value.copy(
                            userData = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _signUpScreenState.value = _signUpScreenState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun loginUser(email: String, password: String) {
        viewModelScope.launch {
            loginUserUseCase.loginUser(email, password).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            userData = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _loginScreenState.value = _loginScreenState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun getUserDataById(userId: String) {
        viewModelScope.launch {
            getUserDataByIdUseCase.getUserDataById(userId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _profileScreenState.value = _profileScreenState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _profileScreenState.value = _profileScreenState.value.copy(
                            userData = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _profileScreenState.value = _profileScreenState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun updateUserData(userDataParent: UserDataParent) {
        viewModelScope.launch {
            updateUserDataUseCase.updateUserData(userDataParent).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _updateScreenState.value = _updateScreenState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _updateScreenState.value = _updateScreenState.value.copy(
                            userData = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _updateScreenState.value = _updateScreenState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun uploadUserProfileImage(uri: Uri) {
        viewModelScope.launch {
            userProfileImageUseCase.uploadProfileImage(uri).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _uploadUserProfileImageState.value =
                            _uploadUserProfileImageState.value.copy(
                                isLoading = true
                            )
                    }

                    is ResultState.Success -> {
                        _uploadUserProfileImageState.value =
                            _uploadUserProfileImageState.value.copy(
                                imageUrl = it.data,
                                isLoading = false
                            )
                    }

                    is ResultState.Error -> {
                        _uploadUserProfileImageState.value =
                            _uploadUserProfileImageState.value.copy(
                                errorMessage = it.message,
                                isLoading = false
                            )
                    }
                }
            }
        }
    }

    fun getProductsInLimit() {
        viewModelScope.launch {
            getProductsInLimitUseCase.getProductsInLimited().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            products = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun getAllProducts() {
        viewModelScope.launch {
            getAllProductUseCase.getProducts().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            products = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getAllProductsState.value = _getAllProductsState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun getProductById(productId: String) {
        viewModelScope.launch {
            getProductByIdUseCase.getProductById(productId).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getProductByIdState.value = _getProductByIdState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getProductByIdState.value = _getProductByIdState.value.copy(
                            product = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getProductByIdState.value = _getProductByIdState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun addToCart(cartDataModel: CartDataModel) {
        viewModelScope.launch {
            addToCartUseCase.addToCart(cartDataModel).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            userData = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _addToCartState.value = _addToCartState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun addToFavourites(productDataModel: ProductDataModel) {
        viewModelScope.launch {
            addToFavUseCase.addToFav(productDataModel).collect {
                when (it) {
                    is ResultState.Loading -> {
                        _addToFavouritesState.value = _addToFavouritesState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _addToFavouritesState.value = _addToFavouritesState.value.copy(
                            userData = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _addToFavouritesState.value = _addToFavouritesState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun getAllFavourites() {
        viewModelScope.launch {
            getAllFavUseCase.getFavourites().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllFavouritesState.value = _getAllFavouritesState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getAllFavouritesState.value = _getAllFavouritesState.value.copy(
                            products = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getAllFavouritesState.value = _getAllFavouritesState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }

    fun getCategoriesInLimit() {
        viewModelScope.launch {
            getCategoriesInLimitUseCase.getCategoriesInLimited().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            categories = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getAllCategoriesState.value = _getAllCategoriesState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }


    fun getAllSuggestedProducts() {
        viewModelScope.launch {
            getAllSuggestedProductUseCase.getAllSuggestedProductUseCase().collect {
                when (it) {
                    is ResultState.Loading -> {
                        _getSuggestedProductsState.value = _getSuggestedProductsState.value.copy(
                            isLoading = true
                        )
                    }

                    is ResultState.Success -> {
                        _getSuggestedProductsState.value = _getSuggestedProductsState.value.copy(
                            products = it.data,
                            isLoading = false
                        )
                    }

                    is ResultState.Error -> {
                        _getSuggestedProductsState.value = _getSuggestedProductsState.value.copy(
                            errorMessage = it.message,
                            isLoading = false
                        )
                    }
                }
            }
        }
    }
}


data class ProfileScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: UserDataParent? = null

)

data class SignUpScreenScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null

)

data class LoginScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null

)

data class UpdateScreenState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null

)

data class UploadUserProfileImageState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val imageUrl: String? = null
)

data class GetProductByIdState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val product: ProductDataModel? = null

)

data class AddToCartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null
)

data class AddToFavouritesState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val userData: String? = null

)

data class GetAllFavouritesState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val products: List<ProductDataModel>? = emptyList()

)

data class GetAllProductsState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val products: List<ProductDataModel>? = emptyList()
)

data class GetCartState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val cart: List<CartDataModel>? = emptyList()
)

data class GetAllCategoriesState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val categories: List<CategoryDataModel>? = emptyList()
)

data class GetCheckoutState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val product: ProductDataModel? = null
)

data class GetSpecificCategoryItemState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val products: List<ProductDataModel>? = emptyList()
)

data class GetSuggestedProductsState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val products: List<ProductDataModel>? = emptyList()
)

