package com.siddroid.begal.core.network

import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.lifecycle.LiveData
import com.siddroid.begal.core.BegalKoinComponent

class NetworkStatusHelper constructor(private val connectivityManager: ConnectivityManager) :
    LiveData<NetworkStatusHelper.NetworkState>(), BegalKoinComponent {

    private var connectivityManagerCallback: ConnectivityManager.NetworkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            super.onAvailable(network)
            setInternetState(true)
        }

        override fun onLost(network: Network) {
            super.onLost(network)
            setInternetState(false)
        }

        override fun onCapabilitiesChanged(
            network: Network,
            networkCapabilities: NetworkCapabilities
        ) {
            val isInternet = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            val isValidated = networkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            setInternetState(isInternet && isValidated)
        }
    }
    private var previousState: NetworkState? = null
    private val networkRequest = NetworkRequest
        .Builder()
        .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
        .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
        .addTransportType(NetworkCapabilities.TRANSPORT_WIFI)
        .addTransportType(NetworkCapabilities.TRANSPORT_CELLULAR)
        .build()

    override fun onActive() {
        super.onActive()
        connectivityManager.registerNetworkCallback(networkRequest, connectivityManagerCallback)
    }

    override fun onInactive() {
        super.onInactive()
        connectivityManager.unregisterNetworkCallback(connectivityManagerCallback)
    }

    private fun setInternetState(isInternetConnected: Boolean) {
        val currentState: NetworkState = if (isInternetConnected) {
            NetworkState.INTERNET_CONNECTED
        } else {
            NetworkState.INTERNET_DISCONNECTED
        }

        previousState?.let {
            if (currentState != previousState) {
                previousState = currentState
                postValue(currentState)
            }
        } ?: kotlin.run {
            previousState = currentState
        }
    }

    enum class NetworkState {
        INTERNET_CONNECTED, INTERNET_DISCONNECTED
    }

    fun isConnected() = previousState == NetworkState.INTERNET_CONNECTED
}