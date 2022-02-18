package de.itshasan.iptv_client.network.service

import de.itshasan.iptv_client.model.SeriesCategories
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SeriesCategoriesService {
    @GET("player_api.php")
    fun getSeriesCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String,
    ): Call<SeriesCategories>
}