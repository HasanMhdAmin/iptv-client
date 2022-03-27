package de.itshasan.iptv_repository.network.callback

abstract class BaseCallback<T> {
    abstract fun onSuccess(backendResponse: T)
    abstract fun onError(status: Int, message: String)
}