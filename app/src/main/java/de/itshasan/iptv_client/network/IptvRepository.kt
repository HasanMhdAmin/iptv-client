package de.itshasan.iptv_client.network

import android.util.Log
import de.itshasan.iptv_client.BuildConfig
import de.itshasan.iptv_client.network.callback.SeriesCategoriesCallback
import de.itshasan.iptv_client.network.service.SeriesCategoriesService
import de.itshasan.iptv_core.model.SeriesCategories
import retrofit2.Call
import retrofit2.Callback
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

        call.enqueue(object : Callback<SeriesCategories> {
            override fun onResponse(
                call: Call<SeriesCategories>,
                response: Response<SeriesCategories>,
            ) {
                callback.onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<SeriesCategories>, t: Throwable) {
                t.printStackTrace()
                Log.e(TAG, "onFailure: ${t.printStackTrace()}")
            }

        })

    }

}