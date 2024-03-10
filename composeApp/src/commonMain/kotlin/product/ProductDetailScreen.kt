package product

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.Navigator
import com.seiko.imageloader.rememberImagePainter
import common.CustomizeAppBar
import data.Product

data class ProductDetailScreen(val product: Product, val navigator: Navigator) : Screen {
    @Composable
    override fun Content() {
        Scaffold(topBar = {
            CustomizeAppBar("Product Detail")
        }) { innerPadding -> PaddingValues()
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .background(Color.White)
                    .fillMaxSize()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                item {
                    val painter = rememberImagePainter(url = product.image.toString())
                    Image(
                        painter = painter,
                        modifier = Modifier.height(120.dp),
                        contentDescription = product.title,
                    )
                    Text(
                        text = "Title",
                        overflow = TextOverflow.Clip,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = product.title.toString(),
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(16.dp).heightIn(min = 40.dp)
                    )
                    Text(
                        text = "Description",
                        overflow = TextOverflow.Clip,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = product.description.toString(),
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(16.dp).heightIn(min = 40.dp)
                    )
                    Text(
                        text = "Price",
                        overflow = TextOverflow.Clip,
                        fontSize = 22.sp,
                        modifier = Modifier.padding(16.dp)
                    )
                    Text(
                        text = "$${product.price.toString()}",
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.padding(16.dp).heightIn(min = 40.dp)
                    )

                }
            }
        }
    }
}