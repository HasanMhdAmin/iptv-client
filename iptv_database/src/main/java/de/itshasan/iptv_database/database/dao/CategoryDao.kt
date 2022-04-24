package de.itshasan.iptv_database.database.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.category.Category

@Dao
interface CategoryDao {
    @Query("SELECT * FROM Category WHERE categoryId == :categoryId")
    fun getCategory(categoryId: String): Category

    @Insert
    fun insert(category: Category)

    @Query("SELECT * FROM Category")
    fun getAll(): List<Category>

    @Query("SELECT * FROM Category WHERE type == \"${Constant.TYPE_SERIES}\"")
    fun getAllSeriesCategory(): List<Category>

    @Query("SELECT * FROM Category WHERE type == \"${Constant.TYPE_MOVIES}\"")
    fun getAllMoviesCategory(): List<Category>

    @Delete
    fun delete(category: Category)

    @Query("DELETE FROM Category")
    fun deleteAll()
}