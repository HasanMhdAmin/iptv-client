package de.itshasan.iptv_network.network

import android.util.Log
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import de.itshasan.iptv_core.model.movie.Movie
import de.itshasan.iptv_core.model.movie.MovieInfo
import de.itshasan.iptv_core.model.series.SeriesList
import de.itshasan.iptv_core.model.category.Category
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_core.model.series.info.info.Info
import de.itshasan.iptv_core.model.series.info.season.Season
import de.itshasan.iptv_core.model.user.User
import de.itshasan.iptv_core.storage.LocalStorage
import de.itshasan.iptv_network.network.callback.*
import de.itshasan.iptv_network.network.enums.Action
import de.itshasan.iptv_network.network.service.LoginService
import de.itshasan.iptv_network.network.service.MoviesService
import de.itshasan.iptv_network.network.service.SeriesService
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
    private val moviesService: MoviesService by lazy {
        retrofit.create(MoviesService::class.java)
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

        val call: Call<ArrayList<Category>> = seriesService.getSeriesCategories(
            LocalStorage.getUsername()!!, LocalStorage.getPassword()!!,
            Action.GET_SERIES_CATEGORIES.value
        )

        val url = call.request().url().toString()
        printURL("getSeriesCategories", url)

        call.enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>,
            ) {
                callback.onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
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

    override fun getMovieStreamUrl(movieId: String, movieExtension: String) =
        "${LocalStorage.getServerUrl()}/movie/${LocalStorage.getUsername()}/${LocalStorage.getPassword()}/$movieId.$movieExtension"

    override fun getMovies(callback: MoviesCallback) {
        val call: Call<ArrayList<Movie>> = moviesService.getMovies(
            LocalStorage.getUsername()!!, LocalStorage.getPassword()!!,
            Action.GET_MOVIES.value
        )

        val url = call.request().url().toString()
        printURL("getMovies", url)

        call.enqueue(object : Callback<ArrayList<Movie>> {
            override fun onResponse(
                call: Call<ArrayList<Movie>>,
                response: Response<ArrayList<Movie>>
            ) {
                callback.onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<Movie>>, t: Throwable) {
                t.printStackTrace()
                Log.e(TAG, "onFailure: ${t.printStackTrace()}")
            }

        })
    }

    override fun getMovieInfo(movieId: String, callback: MovieInfoCallback) {
        val call: Call<MovieInfo> = moviesService.getMovieInfo(
            LocalStorage.getUsername()!!, LocalStorage.getPassword()!!,
            Action.GET_MOVIES_INFO.value, movieId
        )

        val url = call.request().url().toString()
        printURL("getMovieInfo", url)

        call.enqueue(object : Callback<MovieInfo> {
            override fun onResponse(
                call: Call<MovieInfo>,
                response: Response<MovieInfo>
            ) {
                callback.onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<MovieInfo>, t: Throwable) {
                t.printStackTrace()
                Log.e(TAG, "onFailure: ${t.printStackTrace()}")
            }

        })

    }

    override fun getMoviesCategories(callback: CategoriesCallback) {
        val call: Call<ArrayList<Category>> = moviesService.getMoviesCategories(
            LocalStorage.getUsername()!!, LocalStorage.getPassword()!!,
            Action.GET_MOVIES_CATEGORIES.value
        )

        val url = call.request().url().toString()
        printURL("getMoviesCategories", url)

        call.enqueue(object : Callback<ArrayList<Category>> {
            override fun onResponse(
                call: Call<ArrayList<Category>>,
                response: Response<ArrayList<Category>>
            ) {
                callback.onSuccess(response.body()!!)
            }

            override fun onFailure(call: Call<ArrayList<Category>>, t: Throwable) {
                t.printStackTrace()
                Log.e(TAG, "onFailure: ${t.printStackTrace()}")
            }

        })

    }

}