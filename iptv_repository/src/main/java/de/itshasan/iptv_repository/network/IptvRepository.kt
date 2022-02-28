package de.itshasan.iptv_repository.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import de.itshasan.iptv_core.model.series.SeriesList
import de.itshasan.iptv_core.model.series.category.SeriesCategories
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_core.model.series.info.info.Info
import de.itshasan.iptv_core.model.series.info.season.Season
import de.itshasan.iptv_repository.BuildConfig
import de.itshasan.iptv_repository.network.callback.SeriesCallback
import de.itshasan.iptv_repository.network.callback.SeriesCategoriesCallback
import de.itshasan.iptv_repository.network.callback.SeriesInfoCallback
import de.itshasan.iptv_repository.network.enums.Action
import de.itshasan.iptv_repository.network.service.SeriesService
import okhttp3.ResponseBody
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.*


object IptvRepository : IptvRepositoryContract {

    private val TAG = IptvRepository::class.java.simpleName
    private const val baseUrl: String = BuildConfig.IPTV_SERVER
    private const val username: String = BuildConfig.IPTV_USERNAME
    private const val password: String = BuildConfig.IPTV_PASSWORD

    private var gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addConverterFactory(GsonConverterFactory.create(gson))
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

    override fun getSeriesInfoBySeriesId(seriesId: String, callback: SeriesInfoCallback) {
        val call: Call<ResponseBody> = seriesService.getSeriesInfoBySeriesId(username, password,
            Action.GET_SERIES_INFO.value, seriesId)

        val url = call.request().url().toString()
        printURL("getSeriesInfoBySeriesId", url)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {

                val responseJson =  response.body()!!.string()
                val allJson = JSONObject(responseJson)

                val info = gson.fromJson(allJson.getString("info"), Info::class.java)
                val seasonType  = object : TypeToken<List<Season>>() {}.type

                val seasonInfo = gson.fromJson<List<Season>>(allJson.getString("seasons"), seasonType)

                val type = object:TypeToken<Map<String, Object>>(){}.type
                val seasonHashMap = gson.fromJson<Map<String, Object>>(allJson.getString("episodes"), type)
                val episodesJsonObjectWrapper = JSONObject(allJson.getString("episodes"))

                val seasonKeys = seasonHashMap.keys

                val listOfSeason : MutableList<List<Episode>> = mutableListOf()

                val listType = object : TypeToken<List<Episode>>() {}.type
                seasonKeys.map {
                    val listOfSeasonEpisodes = gson.fromJson<List<Episode>>(episodesJsonObjectWrapper.getString(it), listType)
                    listOfSeason.add(listOfSeasonEpisodes)
                }

                val seriesInfo = SeriesInfo(seasonInfo, info, listOfSeason)

                callback.onSuccess(seriesInfo)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                t.printStackTrace()
                Log.e(TAG, "onFailure: ${t.printStackTrace()}")
            }
        })
    }

}