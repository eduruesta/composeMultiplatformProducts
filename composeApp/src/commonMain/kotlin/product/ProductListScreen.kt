package product

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
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.grid.rememberLazyGridState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.ExperimentalMaterialApi
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
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.Navigator
import cafe.adriel.voyager.navigator.currentOrThrow
import com.seiko.imageloader.rememberImagePainter
import common.CustomizeAppBar
import data.Product

class ProductListScreen() : Screen {
    private val viewModel = ProductViewModel()

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        Scaffold(topBar = {
            CustomizeAppBar("Product List")
        }) {
            SetUpViewContent(navigator)
        }
    }

    @Composable
    fun SetUpViewContent(navigator: Navigator) {
        viewModel.products.collectAsState().value.let { state ->
            when (state) {
                is ProductUiState.Loading -> {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp), // Opcional: añade padding alrededor del elemento
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }

                is ProductUiState.Success -> {
                    UpdateProducts(products = state.products, navigator)
                }

                is ProductUiState.Error -> {
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

    @OptIn(ExperimentalMaterialApi::class)
    @Composable
    fun UpdateProducts(products: List<Product>, navigator: Navigator) {
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
                    items(
                        items = products,
                        key = { product -> product.id.toString() }) { product ->
                        Card(
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth()
                                .background(Color.White),
                            onClick = { navigator.push(ProductDetailScreen(product, navigator)) }
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
}
