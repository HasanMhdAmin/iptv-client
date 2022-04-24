package de.itshasan.iptv_database.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import de.itshasan.iptv_core.model.series.category.Category

@Dao
interface SeriesCategoryDao {
    @Query("SELECT * FROM Category WHERE categoryId == :categoryId")
    fun getSeriesCategory(categoryId: String): Category

    @Insert
    fun insert(category: Category)

    @Query("SELECT * FROM Category")
    fun getAll(): List<Category>

    @Delete
    fun delete(category: Category)

    @Query("DELETE FROM Category")
    fun deleteAll()
}