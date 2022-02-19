package de.itshasan.iptv_repository.network

import de.itshasan.iptv_repository.network.callback.SeriesCallback
import de.itshasan.iptv_repository.network.callback.SeriesCategoriesCallback
import de.itshasan.iptv_repository.network.callback.SeriesInfoCallback

interface IptvRepositoryContract {

    fun getSeriesCategories(callback: SeriesCategoriesCallback)
    fun getSeriesByCategoryId(categoryId: String, callback: SeriesCallback)
    fun getSeriesInfoBySeriesId(seriesId: String, callback: SeriesInfoCallback)
}