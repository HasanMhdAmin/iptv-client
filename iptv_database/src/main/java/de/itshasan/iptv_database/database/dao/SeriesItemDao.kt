package de.itshasan.iptv_database.database.dao

import androidx.room.*
import de.itshasan.iptv_core.model.series.SeriesItem

@Dao
interface SeriesItemDao {
    @Query("SELECT * FROM SeriesItem WHERE seriesId == :seriesId")
    fun getSeriesItem(seriesId: String): SeriesItem

    @Query("SELECT * FROM SeriesItem WHERE categoryId == :categoryId")
    fun getSeriesByCategoryId(categoryId: String): List<SeriesItem>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(seriesItem: SeriesItem)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(seriesItems: ArrayList<SeriesItem>)

    @Query("SELECT * FROM SeriesItem")
    fun getAll(): List<SeriesItem>

    @Delete
    fun delete(seriesItem: SeriesItem)

    @Query("DELETE FROM SeriesItem")
    fun deleteAll()
}