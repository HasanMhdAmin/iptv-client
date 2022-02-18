package de.itshasan.iptv_client.network

import android.util.Log
import de.itshasan.iptv_client.BuildConfig
import de.itshasan.iptv_client.model.SeriesCategories
import de.itshasan.iptv_client.network.callback.SeriesCategoriesCallback
import de.itshasan.iptv_client.network.service.SeriesCategoriesService
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IptvRepository {

    private val TAG = IptvRepository::class.java.simpleName
    private const val baseUrl: String = BuildConfig.IPTV_SERVER
    private const val username: String = BuildConfig.IPTV_USERNAME
    private const val password: String = BuildConfig.IPTV_PASSWORD

    fun getSeriesCategories(callback: SeriesCategoriesCallback) {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val service: SeriesCategoriesService = retrofit.create(SeriesCategoriesService::class.java)
        val call: Call<SeriesCategories> = service.getSeriesCategories(username, password,
            Action.GET_SERIES_CATEGORIES.value)

        val url = call.request().url().toString()
        Log.d(TAG, "getSeriesCategories: url: $url")


        try {
            val response: Response<SeriesCategories> = call.execute()
            if (response.code() == 200) { // Success
                callback.onSuccess(response.body()!!)
            } else {
                callback.onError(response.code(), response.message())
            }
        } catch (e: Exception) {
            callback.onError(-1, e.message!!)
        }
    }
}