package expect

import androidx.compose.runtime.Composable

actual val shouldUseSwipeBack: Boolean
    get() = false

actual val showMap: Boolean
    get() = false

@Composable
actual fun GoogleMapKey(lat: String, long: String) {
}