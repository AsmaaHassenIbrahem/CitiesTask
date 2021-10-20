package asma.cities.coordinates.utilities

import android.content.Context
import android.location.Location
import android.location.LocationManager
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.os.Build
import asma.cities.coordinates.EasyParkApplication
import com.google.android.gms.maps.model.LatLng

object NetworkHelper {

    private val mConnectivity =
        EasyParkApplication.applicationContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    /**
     * Check to see if the phone has a data connection - either mobile network or WiFi
     * @return true if the phone has data coverage
     */
    open fun isNetworkConnected(): Boolean =
        mConnectivity.activeNetworkInfo?.isConnected ?: anyNetworkHasInternetCapability()


    private fun anyNetworkHasInternetCapability(): Boolean {
        var internetEnabled: Boolean = false
        mConnectivity.allNetworks.forEach { network ->
            internetEnabled = internetEnabled or hasInternetCapability(network)
        }
        return internetEnabled
    }

    private fun hasInternetCapability(network: Network): Boolean =
        mConnectivity.getNetworkCapabilities(network)?.let { capabilities ->
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) and
                        capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            } else {
                capabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            }
        } ?: false
}

fun LatLng.toLocation() = Location(LocationManager.GPS_PROVIDER).also {
    it.latitude = latitude
    it.longitude = longitude
}

