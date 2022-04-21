package de.itshasan.iptv_network.storage

import android.content.Context
import android.content.SharedPreferences

object LocalStorage {
    private const val PREF_LOCAL_STORAGE = "local_storage"
    private const val PREF_SERVER_URL = "server_url"
    private const val PREF_USERNAME = "username"
    private const val PREF_PASSWORD = "password"

    private var _localStorage: SharedPreferences? = null
    private val localStorage
        get() = _localStorage!!

    fun initPreferences(context: Context) {
        _localStorage = context.getSharedPreferences(PREF_LOCAL_STORAGE, Context.MODE_PRIVATE);
    }

    private fun addPrimitiveToPref(name: String, value: Any) {
        if (_localStorage == null) throw Exception("LocalStorage not initialized")
        when (value) {
            is Int -> {
                localStorage.edit().putInt(name, value).apply()
            }
            is Long -> {
                localStorage.edit().putLong(name, value).apply()
            }
            is Boolean -> {
                localStorage.edit().putBoolean(name, value).apply()
            }
            else -> {
                localStorage.edit().putString(name, value as String).apply()
            }
        }
    }

    fun setServerUrl(serverUrl: String) {
        addPrimitiveToPref(PREF_SERVER_URL, serverUrl)
    }

    fun getServerUrl() = localStorage.getString(PREF_SERVER_URL, "")

    fun setUsername(username: String) {
        addPrimitiveToPref(PREF_USERNAME, username)
    }

    fun getUsername() = localStorage.getString(PREF_USERNAME, "")

    fun setPassword(password: String) {
        addPrimitiveToPref(PREF_PASSWORD, password)
    }

    fun getPassword() = localStorage.getString(PREF_PASSWORD, "")

    fun getUniqueContentId(id: String, category: String) =
        ("${getServerUrl()}.${getUsername()}.${getPassword()}.$category.$id")
            .replace("https://", "")
            .replace("http://", "")
            .replace("/", ".")

    fun getUniqueUserId() =
        "${getServerUrl()}.${getUsername()}.${getPassword()}"
            .replace("https://", "")
            .replace("http://", "")
            .replace("/", ".")
}