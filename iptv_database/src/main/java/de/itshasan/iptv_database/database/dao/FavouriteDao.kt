package de.itshasan.iptv_database.database.dao

import androidx.room.*
import de.itshasan.iptv_core.model.Favourite
import de.itshasan.iptv_core.model.movie.Movie

@Dao
interface FavouriteDao {
    @Query("SELECT * FROM Favourite WHERE uniqueId == :uniqueId")
    fun getFavouriteItem(uniqueId: String): Favourite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(favourite: Favourite)

    @Query("SELECT * FROM Favourite")
    fun getAll(): List<Favourite>

    @Delete
    fun delete(favourite: Favourite)

    @Query("DELETE FROM Favourite")
    fun deleteAll()
}