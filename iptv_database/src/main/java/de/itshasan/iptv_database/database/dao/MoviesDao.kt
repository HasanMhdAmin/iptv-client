package de.itshasan.iptv_database.database.dao

import androidx.room.*
import de.itshasan.iptv_core.model.movie.Movie

@Dao
interface MoviesDao {
    @Query("SELECT * FROM Movie WHERE streamId == :streamId")
    fun getMovie(streamId: String): Movie

    @Query("SELECT * FROM Movie WHERE categoryId == :categoryId")
    fun getMoviesByCategoryId(categoryId: String): List<Movie>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(movie: Movie)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(movie: ArrayList<Movie>)

    @Query("SELECT * FROM Movie")
    fun getAll(): List<Movie>

    @Delete
    fun delete(movie: Movie)

    @Query("DELETE FROM Movie")
    fun deleteAll()
}