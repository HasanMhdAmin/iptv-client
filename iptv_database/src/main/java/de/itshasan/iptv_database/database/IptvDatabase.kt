package de.itshasan.iptv_database.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_core.model.movie.Movie
import de.itshasan.iptv_core.model.series.SeriesItem
import de.itshasan.iptv_core.model.series.category.Category
import de.itshasan.iptv_database.database.dao.MoviesDao
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

@Database(entities = [Category::class, SeriesItem::class, WatchHistory::class, Movie::class],
    version = 3,
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
    abstract fun moviesDao(): MoviesDao
    abstract fun seriesItemDao(): SeriesItemDao
    abstract fun watchHistoryDao(): WatchHistoryDao
}