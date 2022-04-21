package de.itshasan.iptv_client

import android.app.Application
import com.google.firebase.FirebaseApp
import de.itshasan.iptv_database.database.initDatabase
import de.itshasan.iptv_network.storage.LocalStorage.initPreferences

class IptvClient : Application() {

    override fun onCreate() {
        super.onCreate()
        initDatabase(this)
        initPreferences(this)
        FirebaseApp.initializeApp(this)
    }
}