package de.itshasan.iptv_database.database.dao

import androidx.room.*
import de.itshasan.iptv_core.model.WatchHistory

@Dao
interface WatchHistoryDao {
    @Query("SELECT * FROM WatchHistory WHERE contentId == :contentId")
    fun getSeriesItem(contentId: String): WatchHistory

    @Query("SELECT * FROM WatchHistory WHERE parentId == :parentId")
    fun getSeriesByParentId(parentId: String): List<WatchHistory>

    @Query("SELECT * FROM WatchHistory GROUP BY parentId HAVING max(timestamp) Order by timestamp DESC")
    fun getContinueWatching(): List<WatchHistory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(watchHistory: WatchHistory)

    @Query("SELECT * FROM WatchHistory")
    fun getAll(): List<WatchHistory>

    @Delete
    fun delete(watchHistory: WatchHistory)

    @Query("DELETE FROM WatchHistory")
    fun deleteAll()
}