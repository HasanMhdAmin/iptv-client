package de.itshasan.iptv_database.database.di.module

import android.content.Context
import dagger.Module
import dagger.Provides
import de.itshasan.iptv_database.database.IptvDatabase
import javax.inject.Singleton

@Module
internal class DatabaseModule(
    private val context: Context,
) {

    @Provides
    @Singleton
    fun provideContext(): Context {
        return context
    }

    @Provides
    @Singleton
    fun provideDatabase(): IptvDatabase = IptvDatabase.getInstance(context)

}