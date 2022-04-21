package de.itshasan.iptv_network.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.itshasan.iptv_core.model.series.SeriesList
import de.itshasan.iptv_core.model.series.category.SeriesCategories
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_core.model.series.info.info.Info
import de.itshasan.iptv_core.model.series.info.season.Season
import de.itshasan.iptv_core.model.user.User
import de.itshasan.iptv_network.network.callback.LoginCallback
import de.itshasan.iptv_network.network.callback.SeriesCallback
import de.itshasan.iptv_network.network.callback.SeriesCategoriesCallback
import de.itshasan.iptv_network.network.callback.SeriesInfoCallback
import de.itshasan.iptv_network.network.enums.Action
import de.itshasan.iptv_network.network.service.LoginService
import de.itshasan.iptv_network.network.service.SeriesService
import de.itshasan.iptv_network.storage.LocalStorage
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object IptvNetwork : IptvNetworkContract {

    private val TAG = IptvNetwork::class.java.simpleName

    private var gson = GsonBuilder()
        .setLenient()
        .create()
    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(LocalStorage.getServerUrl())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }
    private val seriesService: SeriesService by lazy {
        retrofit.create(SeriesService::class.java)
    }

    private fun printURL(method: String, url: String) {
        Log.d(TAG, "$method: url: $url")
    }

    override fun login(
        serverUrl: String,
        username: String,
        password: String,
        callback: LoginCallback
    ) {
        try {
            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(serverUrl)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()

            val loginService: LoginService = retrofit.create(LoginService::class.java)

            val call: Call<User> = loginService.loginUser(username, password)

            val url = call.request().url().toString()
            printURL("login", url)

            call.enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    if (response.code() == 200)
                        callback.onSuccess(response.body()!!)
                    else
                        callback.onError(response.code(), response.message())
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    t.printStackTrace()
                    Log.e(TAG, "onFailure: ${t.printStackTrace()}")
                }
            })
        } catch (ex: Exception) {
            callback.onError(-1, ex.message ?: "")
        }

    }

    override fun getSeriesCategories(callback: SeriesCategoriesCallback) {

        val call: Call<SeriesCategories> = seriesService.getSeriesCategories(
            LocalStorage.getUsername()!!, LocalStorage.getPassword()!!,
            Action.GET_SERIES_CATEGORIES.value
        )

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

        val call: Call<SeriesList> = seriesService.getSeriesByCategoryId(
            LocalStorage.getUsername()!!, LocalStorage.getPassword()!!,
            Action.GET_SERIES.value, categoryId
        )

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
        val call: Call<ResponseBody> = seriesService.getSeriesInfoBySeriesId(
            LocalStorage.getUsername()!!, LocalStorage.getPassword()!!,
            Action.GET_SERIES_INFO.value, seriesId
        )

        val url = call.request().url().toString()
        printURL("getSeriesInfoBySeriesId", url)

        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(
                call: Call<ResponseBody>,
                response: Response<ResponseBody>,
            ) {

                val responseJson = response.body()!!.string()
                val allJson = JSONObject(responseJson)

                val info = gson.fromJson(allJson.getString("info"), Info::class.java)
                val seasonType = object : TypeToken<List<Season>>() {}.type

                val type = object : TypeToken<Map<String, Object>>() {}.type
                val seasonHashMap =
                    gson.fromJson<Map<String, Object>>(allJson.getString("episodes"), type)
                val episodesJsonObjectWrapper = JSONObject(allJson.getString("episodes"))

                val seasonKeys = seasonHashMap.keys

                val listOfSeason: MutableList<List<Episode>> = mutableListOf()

                val listType = object : TypeToken<List<Episode>>() {}.type
                seasonKeys.map {
                    val listOfSeasonEpisodes = gson.fromJson<List<Episode>>(
                        episodesJsonObjectWrapper.getString(it),
                        listType
                    )
                    listOfSeason.add(listOfSeasonEpisodes)
                }

                var seasonInfo =
                    gson.fromJson<MutableList<Season>>(allJson.getString("seasons"), seasonType)
                // build season list if not available
                if (seasonInfo == null || seasonInfo.isEmpty()) {
                    seasonInfo = mutableListOf()
                    listOfSeason.forEachIndexed { index, episodes ->
                        seasonInfo.add(
                            Season(
                                airDate = "N/A",
                                episodeCount = episodes.size,
                                id = -1,
                                name = "Season ${index + 1}",
                                overview = "",
                                seasonNumber = index + 1,
                                cover = info.cover,
                                coverBig = info.cover
                            )
                        )
                    }
                }
                val toRemove = seasonInfo.firstOrNull { it.seasonNumber == 0 }
                // remove not wanted entry. (tested on The Big Bang Theory)
                toRemove?.let {
                    seasonInfo.remove(it)
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

    override fun getEpisodeStreamUrl(episodeId: String, episodeExtension: String) =
        "${LocalStorage.getServerUrl()}/series/${LocalStorage.getUsername()}/${LocalStorage.getPassword()}/$episodeId.$episodeExtension"

}