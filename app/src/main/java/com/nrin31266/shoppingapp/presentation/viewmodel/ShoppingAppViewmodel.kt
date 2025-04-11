package com.nrin31266.shoppingapp.presentation.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.nrin31266.shoppingapp.domain.model.CartDataModel
import com.nrin31266.shoppingapp.domain.model.CategoryDataModel
import com.nrin31266.shoppingapp.domain.model.ProductDataModel
import com.nrin31266.shoppingapp.domain.model.UserDataParent
import com.nrin31266.shoppingapp.domain.use_case.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
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
):ViewModel() {
    
    private val _profileScreenState = MutableStateFlow(ProfileScreenState())
    val profileScreenState: StateFlow<ProfileScreenState> = _profileScreenState.asStateFlow()

    private val _signUpScreenState = MutableStateFlow(SignUpScreenScreenState())
    val signUpScreenState: StateFlow<SignUpScreenScreenState> = _signUpScreenState.asStateFlow()

    private val _loginScreenState = MutableStateFlow(LoginScreenState())
    val loginScreenState: StateFlow<LoginScreenState> = _loginScreenState.asStateFlow()

    private val _updateScreenState = MutableStateFlow(UpdateScreenState())
    val updateScreenState: StateFlow<UpdateScreenState> = _updateScreenState.asStateFlow()

    private val _uploadUserProfileImageState = MutableStateFlow(UploadUserProfileImageState())
    val uploadUserProfileImageState: StateFlow<UploadUserProfileImageState> = _uploadUserProfileImageState.asStateFlow()

    private val _getProductByIdState = MutableStateFlow(GetProductByIdState())
    val getProductByIdState: StateFlow<GetProductByIdState> = _getProductByIdState.asStateFlow()

    private val _addToCartState = MutableStateFlow(AddToCartState())
    val addToCartState: StateFlow<AddToCartState> = _addToCartState.asStateFlow()

    private val _addToFavouritesState = MutableStateFlow(AddToFavouritesState())
    val addToFavouritesState: StateFlow<AddToFavouritesState> = _addToFavouritesState.asStateFlow()

    private val _getAllFavouritesState = MutableStateFlow(GetAllFavouritesState())
    val getAllFavouritesState: StateFlow<GetAllFavouritesState> = _getAllFavouritesState.asStateFlow()

    private val _getAllProductsState = MutableStateFlow(GetAllProductsState())
    val getAllProductsState: StateFlow<GetAllProductsState> = _getAllProductsState.asStateFlow()

    private val _getCartState = MutableStateFlow(GetCartState())
    val getCartState: StateFlow<GetCartState> = _getCartState.asStateFlow()

    private val _getAllCategoriesState = MutableStateFlow(GetAllCategoriesState())
    val getAllCategoriesState: StateFlow<GetAllCategoriesState> = _getAllCategoriesState.asStateFlow()

    private val _getCheckoutState = MutableStateFlow(GetCheckoutState())
    val getCheckoutState: StateFlow<GetCheckoutState> = _getCheckoutState.asStateFlow()

    private val _getSpecificCategoryItemState = MutableStateFlow(GetSpecificCategoryItemState())
    val getSpecificCategoryItemState: StateFlow<GetSpecificCategoryItemState> = _getSpecificCategoryItemState.asStateFlow()

    private val _getSuggestedProductsState = MutableStateFlow(GetSuggestedProductsState())
    val getSuggestedProductsState: StateFlow<GetSuggestedProductsState> = _getSuggestedProductsState.asStateFlow()
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

