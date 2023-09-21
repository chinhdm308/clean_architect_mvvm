package com.base.data.remote

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class NetworkEvent @Inject constructor() {

    private val _networkState = MutableSharedFlow<NetworkState>()
    val networkState = _networkState.asSharedFlow()

    fun publish(event: NetworkState) = CoroutineScope(Dispatchers.Main).launch {
        _networkState.emit(event)
    }
}

sealed class NetworkState {
    data object AvailableInternet : NetworkState()
    data object NoInternet : NetworkState()
    data object ServerNotAvailable : NetworkState() // 503
    data object NotFound : NetworkState() // 404
    data object Forbidden : NetworkState() // 403
    data object BadRequest : NetworkState() // 400
    data object UnAuthorized : NetworkState() // 401
    data object ConnectionLost : NetworkState()
    data object Error : NetworkState()
    data object Initialize : NetworkState()
    data class Generic(val apiException: Exception) : NetworkState()
    data object ConnectExceptionPostMethod : NetworkState()
    data object ConnectExceptionGetMethod : NetworkState()
}
