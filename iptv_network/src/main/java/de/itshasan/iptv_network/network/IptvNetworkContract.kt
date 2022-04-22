package de.itshasan.iptv_network.network

import de.itshasan.iptv_network.network.callback.*

interface IptvNetworkContract {

    fun login(
        serverUrl: String,
        username: String,
        password: String, callback: LoginCallback
    )

    fun getSeriesCategories(callback: SeriesCategoriesCallback)
    fun getSeriesByCategoryId(categoryId: String, callback: SeriesCallback)
    fun getSeriesInfoBySeriesId(seriesId: String, callback: SeriesInfoCallback)
    fun getEpisodeStreamUrl(episodeId: String, episodeExtension: String): String
    fun getMovieStreamUrl(movieId: String, movieExtension: String): String

    fun getMovies(callback: MoviesCallback)
    fun getMovieInfo(movieId: String, callback: MovieInfoCallback)
}