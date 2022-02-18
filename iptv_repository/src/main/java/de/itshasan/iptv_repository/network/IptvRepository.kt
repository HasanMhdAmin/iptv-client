package de.itshasan.iptv_repository.network

import android.util.Log
import de.itshasan.iptv_core.model.SeriesCategories
import de.itshasan.iptv_repository.BuildConfig
import de.itshasan.iptv_repository.network.callback.SeriesCategoriesCallback
import de.itshasan.iptv_repository.network.enums.Action
import de.itshasan.iptv_repository.network.service.SeriesCategoriesService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IptvRepository : IptvRepositoryContract {

    private val TAG = IptvRepository::class.java.simpleName
    private const val baseUrl: String = BuildConfig.IPTV_SERVER
    private const val username: String = BuildConfig.IPTV_USERNAME
    private const val password: String = BuildConfig.IPTV_PASSWORD

    override fun getSeriesCategories(callback: SeriesCategoriesCallback) {
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