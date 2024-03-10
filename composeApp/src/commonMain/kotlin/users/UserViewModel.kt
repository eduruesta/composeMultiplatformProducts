package users

import data.UserItem
import dev.icerock.moko.mvvm.viewmodel.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


sealed class UserUiState {
    object Loading : UserUiState()
    data class Success(val users: List<UserItem>) : UserUiState()
    data class Error(val error: String) : UserUiState()
}

class UserViewModel : ViewModel() {

    private val _usersState = MutableStateFlow<UserUiState>(UserUiState.Loading)
    val users: StateFlow<UserUiState> = _usersState

    private val userRepository = UserRepository()

    init {
        try {
            viewModelScope.launch {
                _usersState.value = UserUiState.Loading

                userRepository.getUsers().collect { users ->
                    _usersState.update { UserUiState.Success(users) }
                }
            }
        } catch (e: Exception) {
            _usersState.update { UserUiState.Error(e.message.toString()) }
        }
    }

    // Función para obtener los usuarios desde la actividad
    fun getUsers(): List<UserItem>? {
        return when (val state = _usersState.value) {
            is UserUiState.Success -> state.users
            else -> null
        }
    }
}
