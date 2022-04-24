package de.itshasan.iptv_network.network.service

import de.itshasan.iptv_core.model.movie.Movie
import de.itshasan.iptv_core.model.movie.MovieInfo
import de.itshasan.iptv_core.model.category.Category
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MoviesService {

    @GET("player_api.php")
    fun getMovies(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String,
    ): Call<ArrayList<Movie>>

    @GET("player_api.php")
    fun getMovieInfo(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String,
        @Query("vod_id") series_id: String,
    ): Call<MovieInfo>

    @GET("player_api.php")
    fun getMoviesCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String,
    ): Call<ArrayList<Category>>
}