package de.itshasan.iptv_database.database.dao

import androidx.room.*
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_core.model.series.SeriesItem

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM WatchHistory WHERE contentId == :contentId")
    fun getSeriesItem(contentId: String): WatchHistory

    @Query("SELECT * FROM WatchHistory WHERE parentId == :parentId")
    fun getSeriesByParentId(parentId: String): List<WatchHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(watchHistory: WatchHistory)

    @Query("SELECT * FROM WatchHistory")
    fun getAll(): List<WatchHistory>

    @Delete
    fun delete(watchHistory: WatchHistory)

    @Query("DELETE FROM WatchHistory")
    fun deleteAll()
}