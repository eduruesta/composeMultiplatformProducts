package users

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme.typography
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color.Companion.Gray
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.screen.Screen
import common.CustomizeAppBar
import data.Geolocation
import expect.GoogleMapKey
import expect.showMap

class UserLocation(private val userLocation: Geolocation?) : Screen {

    @Composable
    override fun Content() {
        Scaffold(topBar = {
            CustomizeAppBar("User Location")
        }) {
            UserLocationContent(userLocation)
        }
    }

    @Composable
    fun UserLocationContent(userLocation: Geolocation?) {
        if (showMap) {
            GoogleMapKey(userLocation?.lat.toString(), userLocation?.long.toString())
        } else {
            Column(
                modifier = Modifier
                    .padding(30.dp)
                    .fillMaxWidth()
                    .fillMaxHeight()
                    .wrapContentSize(Alignment.Center)
                    .clip(shape = RoundedCornerShape(16.dp)),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .border(width = 4.dp, color = Gray, shape = RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "The location is: ${userLocation?.lat}, ${userLocation?.long}",
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center,
                        style = typography.h4,
                    )
                }
            }
        }
    }
}