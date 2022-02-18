package de.itshasan.iptv_repository.network

import android.util.Log
import de.itshasan.iptv_core.model.series.SeriesList
import de.itshasan.iptv_core.model.series.category.SeriesCategories
import de.itshasan.iptv_repository.BuildConfig
import de.itshasan.iptv_repository.network.callback.SeriesCallback
import de.itshasan.iptv_repository.network.callback.SeriesCategoriesCallback
import de.itshasan.iptv_repository.network.enums.Action
import de.itshasan.iptv_repository.network.service.SeriesService
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

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val seriesService: SeriesService = retrofit.create(SeriesService::class.java)

    private fun printURL(method: String, url: String) {
        Log.d(TAG, "$method: url: $url")
    }

    override fun getSeriesCategories(callback: SeriesCategoriesCallback) {

        val call: Call<SeriesCategories> = seriesService.getSeriesCategories(username, password,
            Action.GET_SERIES_CATEGORIES.value)

        val url = call.request().url().toString()
        printURL("getSeriesCategories", url)

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

    override fun getSeriesByCategoryId(categoryId: String, callback: SeriesCallback) {

        val call: Call<SeriesList> = seriesService.getSeriesByCategoryId(username, password,
            Action.GET_SERIES.value, categoryId)

        val url = call.request().url().toString()
        printURL("getSeriesByCategoryId", url)

        call.enqueue(object : Callback<SeriesList> {
            override fun onResponse(
                call: Call<SeriesList>,
                response: Response<SeriesList>,
            ) {
                callback.onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<SeriesList>, t: Throwable) {
                t.printStackTrace()
                Log.e(TAG, "onFailure: ${t.printStackTrace()}")
            }

        })


    }

}