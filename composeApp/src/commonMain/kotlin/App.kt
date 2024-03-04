import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.SnackbarHost
import androidx.compose.material.rememberScaffoldState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.seiko.imageloader.rememberImagePainter
import data.Product
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        AppContent(viewModel = HomeViewModel())

    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppContent(viewModel: HomeViewModel) {
    viewModel.products.collectAsState().value.let { state ->
        when (state) {
            is HomeUiState.Loading -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp), // Opcional: añade padding alrededor del elemento
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator()
                }
            }
            is HomeUiState.Success -> {
                // Obtener los productos y actualizar la UI
                UpdateProducts(state.products)
                // Hacer algo con los productos, como mostrarlos en una lista
            }
            is HomeUiState.Error -> {
                val context = currentCompositionLocalContext
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProducts(products: List<Product>) {
    BoxWithConstraints {
        val scope = this
        val maxWidth = scope.maxWidth
        var col = 2
        var modifier = Modifier.fillMaxWidth()

        if (maxWidth > 840.dp) {
            col = 3
            modifier = Modifier.widthIn(max = 1080.dp)
        }
        val scrollState = rememberLazyGridState()
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(col),
                modifier = modifier,
                state = scrollState,
                contentPadding = PaddingValues(16.dp)
            ) {

                item(span = { GridItemSpan(col) }) {
                    SearchBar(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.White),
                        query = "",
                        active = false,
                        onActiveChange = {},
                        onQueryChange = {},
                        onSearch = {}
                    ) {
                        Text(text = "Search")
                    }
                }
                items(
                    items = products,
                    key = { product -> product.id.toString() }) { product ->
                    Card(
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(16.dp)
                            .fillMaxWidth()
                            .background(Color.White),
                        elevation = CardDefaults.cardElevation(
                            defaultElevation = 2.dp
                        )
                    ) {
                        Column(
                            modifier = Modifier.background(Color.White),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            val painter = rememberImagePainter(url = product.image.toString())
                            Image(
                                painter = painter,
                                modifier = Modifier.height(120.dp),
                                contentDescription = product.title
                            )
                            Text(
                                text = product.title.toString(),
                                maxLines = 2,
                                overflow = TextOverflow.Ellipsis,
                                modifier = Modifier.padding(16.dp).heightIn(min = 40.dp)
                            )
                        }

                    }

                }

            }
        }

    }
}