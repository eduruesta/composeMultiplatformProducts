package expect

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

actual val shouldUseSwipeBack: Boolean
    get() = false

actual val showMap: Boolean
    get() = true

@Composable
actual fun GoogleMapKey(lat: String, long: String) {
    val location = LatLng(lat.toDouble(), long.toDouble())
    val markerState = MarkerState(position = location)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(location, 5f)
    }
    GoogleMap(
        modifier = Modifier.fillMaxSize(),
        cameraPositionState = cameraPositionState
    ) {
        Marker(state = markerState, title = "Ahi esta el condo")
    }

}