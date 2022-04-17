package de.itshasan.iptv_client

import android.app.Application
import de.itshasan.iptv_database.database.initDatabase
import de.itshasan.iptv_repository.storage.LocalStorage.initPreferences

class IptvClient : Application() {

    override fun onCreate() {
        super.onCreate()
        initDatabase(this)
        initPreferences(this)
    }
}