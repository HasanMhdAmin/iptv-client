package de.itshasan.iptv_database.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import de.itshasan.iptv_core.model.series.category.SeriesCategoriesItem

@Dao
interface SeriesCategoryDao {
    @Query("SELECT * FROM SeriesCategoriesItem WHERE categoryId == :categoryId")
    fun getSeriesCategory(categoryId: String): SeriesCategoriesItem

    @Insert
    fun insert(seriesCategoriesItem: SeriesCategoriesItem)

    @Query("SELECT * FROM SeriesCategoriesItem")
    fun getAll(): List<SeriesCategoriesItem>

    @Delete
    fun delete(seriesCategoriesItem: SeriesCategoriesItem)

    @Query("DELETE FROM SeriesCategoriesItem")
    fun deleteAll()
}