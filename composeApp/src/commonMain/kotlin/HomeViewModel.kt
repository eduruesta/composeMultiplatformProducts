import data.Product
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    private val _productsState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val products: StateFlow<HomeUiState> = _productsState

    private val homeRepository = HomeRepository()

    init {
        try {
            viewModelScope.launch {
                _productsState.value = HomeUiState.Loading
                delay(4000)

                homeRepository.getProducts().collect { products ->
                    _productsState.update { HomeUiState.Success(products) }
                }
            }
        } catch (e: Exception) {
            _productsState.update { HomeUiState.Error(e.message.toString()) }
        }
    }

    // Función para obtener los productos desde la actividad
    fun getProducts(): List<Product>? {
        return when (val state = _productsState.value) {
            is HomeUiState.Success -> state.products
            else -> null
        }
    }

}

sealed class HomeUiState {
    object Loading : HomeUiState()
    data class Success(val products: List<Product>) : HomeUiState()
    data class Error(val error: String) : HomeUiState()
}