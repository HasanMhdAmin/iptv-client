package de.itshasan.iptv_network.network

import de.itshasan.iptv_network.network.callback.LoginCallback
import de.itshasan.iptv_network.network.callback.SeriesCallback
import de.itshasan.iptv_network.network.callback.SeriesCategoriesCallback
import de.itshasan.iptv_network.network.callback.SeriesInfoCallback

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
}