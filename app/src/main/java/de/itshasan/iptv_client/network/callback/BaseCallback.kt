package de.itshasan.iptv_client.network.callback

abstract class BaseCallback<T> {
    abstract fun onSuccess(backendResponse: T)
    abstract fun onError(status: Int, message: String)
}