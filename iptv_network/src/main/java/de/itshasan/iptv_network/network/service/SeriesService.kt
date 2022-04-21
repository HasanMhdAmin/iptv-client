package de.itshasan.iptv_network.network.service

import de.itshasan.iptv_core.model.series.SeriesList
import de.itshasan.iptv_core.model.series.category.SeriesCategories
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface SeriesService {
    @GET("player_api.php")
    fun getSeriesCategories(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String,
    ): Call<SeriesCategories>

    @GET("player_api.php")
    fun getSeriesByCategoryId(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String,
        @Query("category_id") categoryId: String,
    ): Call<SeriesList>

    @GET("player_api.php")
    fun getSeriesInfoBySeriesId(
        @Query("username") username: String,
        @Query("password") password: String,
        @Query("action") action: String,
        @Query("series_id") series_id: String,
    ): Call<ResponseBody>
}