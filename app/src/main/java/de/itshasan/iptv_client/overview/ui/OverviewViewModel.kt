package de.itshasan.iptv_client.overview.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_core.model.series.info.season.Season
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.SeriesInfoCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val TAG = OverviewViewModel::class.java.simpleName

class OverviewViewModel(seriesId: Int) : ViewModel() {

    var seriesInfo: MutableLiveData<SeriesInfo> = MutableLiveData<SeriesInfo>()
    var contentName: MutableLiveData<String> = MutableLiveData<String>()
    var releaseDate: MutableLiveData<String> = MutableLiveData<String>()
    var plot: MutableLiveData<String> = MutableLiveData<String>()
    var cast: MutableLiveData<String> = MutableLiveData<String>()
    var director: MutableLiveData<String> = MutableLiveData<String>()
    var coverImageUrl: MutableLiveData<String> = MutableLiveData<String>()
    var seasons = MutableLiveData<List<Season>>()
    var allEpisodes = MutableLiveData<List<List<Episode>>>()
    var episodesToShow = MutableLiveData<List<Episode>>()
    var selectedSeason = MutableLiveData<Season>()
    var currentEpisodeProgress = MutableLiveData<Pair<Episode, WatchHistory?>>()

    init {
        makeAPICall(seriesId)
    }

    fun setSelectedSeason(season: Season) {
        selectedSeason.postValue(season)
        val index: Int = seasons.value!!.indexOf(season)
        episodesToShow.postValue(allEpisodes.value!![index])
    }


    fun updateWatchHistory(seriesId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val watchHistory =
                iptvDatabase.watchHistoryDao()
                    .getSeriesByParentIdOrderByTimestamp(seriesId.toString()).firstOrNull()
            if (watchHistory != null) {
                Log.d(TAG, "onSuccess: watchHistory != null")

                run breaking@{
                    allEpisodes.value!!.forEachIndexed { index, list ->
                        val item = list.find { item -> item.id == watchHistory.contentId }
                        if (item != null) {
                            Log.d(TAG, "onSuccess: item != null")
                            currentEpisodeProgress.postValue(Pair(item, watchHistory))
                            setSelectedSeason(seasons.value!![index])
                            return@breaking
                        } else {
                            Log.d(TAG, "onSuccess: item == null")
                        }
                    }
                }
            } else {
                Log.d(TAG, "onSuccess: watchHistory == null")
                currentEpisodeProgress.postValue(Pair(allEpisodes.value!![0][0], null))
            }
        }
    }

    private fun makeAPICall(seriesId: Int) {

        IptvRepository.getSeriesInfoBySeriesId(seriesId.toString(), object : SeriesInfoCallback() {
            override fun onSuccess(backendResponse: SeriesInfo) {
                // TODO is postValue assign the value also?
                seriesInfo.postValue(backendResponse)
                contentName.postValue(backendResponse.info.name)
                releaseDate.postValue(backendResponse.info.releaseDate)
                plot.postValue(backendResponse.info.plot)
                cast.postValue(backendResponse.info.cast)
                director.postValue(backendResponse.info.director)
                coverImageUrl.postValue(backendResponse.info.cover)
                seasons.postValue(backendResponse.seasons)
                seasons.value = backendResponse.seasons
                allEpisodes.postValue(backendResponse.episodes)
                allEpisodes.value = backendResponse.episodes
                episodesToShow.postValue(backendResponse.episodes[0])

                setSelectedSeason(backendResponse.seasons[0])
                updateWatchHistory(seriesId)
            }

            override fun onError(status: Int, message: String) {
                contentName.postValue("Hasan")
            }
        })
    }
}