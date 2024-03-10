package expect

import androidx.compose.runtime.Composable

expect val shouldUseSwipeBack: Boolean
expect val showMap: Boolean
@Composable
expect fun GoogleMapKey(lat: String, long: String)
