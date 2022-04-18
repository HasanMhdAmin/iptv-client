package de.itshasan.iptv_database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_core.model.series.SeriesItem
import de.itshasan.iptv_core.model.series.category.SeriesCategoriesItem
import de.itshasan.iptv_database.database.dao.SeriesCategoryDao
import de.itshasan.iptv_database.database.dao.SeriesItemDao
import de.itshasan.iptv_database.database.dao.WatchHistoryDao
import de.itshasan.iptv_database.database.di.DaggerDatabaseComponent
import de.itshasan.iptv_database.database.di.DatabaseComponent
import de.itshasan.iptv_database.database.di.module.DatabaseModule

private var component: DatabaseComponent? = null

val iptvDatabase: IptvDatabase
    get() = component!!.getDatabase()

fun initDatabase(context: Context) {
    if (component == null) {
        component = DaggerDatabaseComponent.builder()
            .databaseModule(DatabaseModule(context))
            .build()
    }
}

@Database(entities = [SeriesCategoriesItem::class, SeriesItem::class, WatchHistory::class],
    version = 14,
    exportSchema = false)
abstract class IptvDatabase : RoomDatabase() {
    companion object {

        @Volatile
        private var INSTANCE: IptvDatabase? = null

        fun getInstance(context: Context): IptvDatabase =
            INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }

        private fun buildDatabase(context: Context) =
            Room.databaseBuilder(
                context.applicationContext,
                IptvDatabase::class.java, "iptv-database.db"
            )
                .fallbackToDestructiveMigration()
                .build()
    }

    abstract fun seriesCategoryDao(): SeriesCategoryDao
    abstract fun seriesItemDao(): SeriesItemDao
    abstract fun watchHistoryDao(): WatchHistoryDao
}