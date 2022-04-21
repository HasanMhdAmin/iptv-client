package de.itshasan.iptv_network.network.service

import de.itshasan.iptv_core.model.movie.Movie
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
}