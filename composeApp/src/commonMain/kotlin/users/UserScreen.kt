package users

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import common.CustomizeAppBar
import data.UserItem

class UserScreen : Screen {
    private val userViewModel = UserViewModel()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(topBar = {
            CustomizeAppBar("User List")
        }) {
            userViewModel.users.collectAsState().value.let { state ->
                when (state) {
                    is UserUiState.Loading -> {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp), // Opcional: añade padding alrededor del elemento
                            contentAlignment = Alignment.Center
                        ) {
                             CircularProgressIndicator()
                        }
                    }

                    is UserUiState.Success -> {
                        ShowUserList(state.users, navigator)
                    }

                    is UserUiState.Error -> {
                        val scaffoldState = rememberScaffoldState()
                        LaunchedEffect(key1 = scaffoldState) {
                            scaffoldState.snackbarHostState.showSnackbar(message = state.error)
                        }
                        Box(modifier = Modifier.fillMaxSize()) {
                            Scaffold(
                                scaffoldState = scaffoldState,
                                snackbarHost = { SnackbarHost(hostState = scaffoldState.snackbarHostState) {} }
                            ) {}
                        }
                    }
                }
            }
        }
    }

    @Composable
    fun ShowUserList(users: List<UserItem>, navigator: Navigator) {
        // Show user list
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
            contentPadding = PaddingValues(8.dp)
        ) {
            items(users) { user ->
                // Show user item
                 Card(
                    border = BorderStroke(1.dp, Color.Black),
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                ) {
                     Column {
                         // Show user item content
                         val name = user.name?.firstname + " " + user.name?.lastname
                         Text(text = name, modifier = Modifier.padding(8.dp))
                         Text(text = user.email ?: "", modifier = Modifier.padding(8.dp))
                         Text(text = user.phone ?: "", modifier = Modifier.padding(8.dp))
                         Button(onClick = {
                             navigator.push(
                                 UserLocation(user.address?.geolocation)
                             )
                         }, modifier = Modifier.padding(8.dp)) {
                             Text(text = "Show location", color = Color.Black)
                         }
                     }
                }
            }
        }
    }
}
