package product

import data.Product
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ProductViewModel : ViewModel() {

    private val _productsState = MutableStateFlow<ProductUiState>(ProductUiState.Loading)
    val products: StateFlow<ProductUiState> = _productsState

    private val productRepository = ProductRepository()

    init {
        try {
            viewModelScope.launch {
                _productsState.value = ProductUiState.Loading

                productRepository.getProducts().collect { products ->
                    _productsState.update { ProductUiState.Success(products) }
                }
            }
        } catch (e: Exception) {
            _productsState.update { ProductUiState.Error(e.message.toString()) }
        }
    }

    // Función para obtener los productos desde la actividad
    fun getProducts(): List<Product>? {
        return when (val state = _productsState.value) {
            is ProductUiState.Success -> state.products
            else -> null
        }
    }

}

sealed class ProductUiState {
    object Loading : ProductUiState()
    data class Success(val products: List<Product>) : ProductUiState()
    data class Error(val error: String) : ProductUiState()
}