package pt.ulusofona.deisi.cm2223.g22004883_22007130

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest

class NetworkListener(private val context: Context) {

    private val TAG = NetworkListener::class.java.simpleName

    private val networkRequest: NetworkRequest =
        NetworkRequest.Builder()
            .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
            .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
            .addTransportType(NetworkCapabilities.TRANSPORT_ETHERNET)
            .build()

    private val connectivityManager: ConnectivityManager =
        context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

    private var networkCallback: ConnectivityManager.NetworkCallback? = null

    fun startListening(listener: NetworkStateListener) {
        networkCallback = createNetworkCallback(listener)
        connectivityManager.registerNetworkCallback(networkRequest, networkCallback!!)
    }

    fun stopListening() {
        networkCallback?.let {
            connectivityManager.unregisterNetworkCallback(it)
            networkCallback = null
        }
    }

    private fun createNetworkCallback(listener: NetworkStateListener): ConnectivityManager.NetworkCallback {
        return object : ConnectivityManager.NetworkCallback() {
            override fun onAvailable(network: Network) {
                super.onAvailable(network)
                listener.onNetworkAvailable()
            }

            override fun onLost(network: Network) {
                super.onLost(network)
                listener.onNetworkLost()
            }

            override fun onUnavailable() {
                super.onUnavailable()
                listener.onNetworkUnavailable()
            }
        }
    }
}

interface NetworkStateListener {
    fun onNetworkAvailable()
    fun onNetworkLost()
    fun onNetworkUnavailable()
}
