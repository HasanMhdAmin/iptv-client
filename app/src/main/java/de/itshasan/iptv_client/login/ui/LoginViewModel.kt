package de.itshasan.iptv_client.login.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.itshasan.iptv_core.model.user.User
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.LoginCallback
import de.itshasan.iptv_repository.storage.LocalStorage

private const val TAG = "LoginViewModel"

class LoginViewModel : ViewModel() {

    var user: MutableLiveData<User?> = MutableLiveData<User?>()

    private lateinit var serverUrl: String
    private lateinit var username: String
    private lateinit var password: String

    fun requestLoginUser(serverUrl: String, username: String, password: String) {
        IptvRepository.login(serverUrl, username, password, object : LoginCallback() {
            override fun onSuccess(backendResponse: User) {
                Log.d(TAG, "onSuccess: ")
                this@LoginViewModel.serverUrl = serverUrl
                this@LoginViewModel.username = username
                this@LoginViewModel.password = password
                user.postValue(backendResponse)
            }

            override fun onError(status: Int, message: String) {
                Log.d(TAG, "onError: $message")
                user.postValue(null)
            }

        })
    }

    fun saveUserCredential() {
        LocalStorage.setServerUrl(serverUrl)
        LocalStorage.setUsername(username)
        LocalStorage.setPassword(password)
    }

}