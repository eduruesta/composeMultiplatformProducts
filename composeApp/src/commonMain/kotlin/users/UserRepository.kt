package users

import apiClient.httpClient
import data.UserItem
import io.ktor.client.call.body
import io.ktor.client.request.get
import kotlinx.coroutines.flow.flow

class UserRepository {

    private suspend fun getUsersApi(): List<UserItem> {
        val response = httpClient.get("https://fakestoreapi.com/users")
        return response.body()
    }

    fun getUsers() = flow {
        emit(getUsersApi())
    }
}