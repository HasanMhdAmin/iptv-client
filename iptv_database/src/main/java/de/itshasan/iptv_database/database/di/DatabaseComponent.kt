package de.itshasan.iptv_database.database.di

import dagger.Component
import de.itshasan.iptv_database.database.IptvDatabase
import de.itshasan.iptv_database.database.di.module.DatabaseModule
import javax.inject.Singleton

@Singleton
@Component(modules = [DatabaseModule::class])
internal interface DatabaseComponent {
    fun getDatabase(): IptvDatabase
}