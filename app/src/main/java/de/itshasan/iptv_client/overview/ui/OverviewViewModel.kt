package de.itshasan.iptv_client.overview.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_core.model.series.info.season.Season
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.SeriesInfoCallback

private val TAG = OverviewViewModel::class.java.simpleName

class OverviewViewModel(seriesId: Int) : ViewModel() {

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

    init {
        makeAPICall(seriesId)
    }

    fun setSelectedSeason(season: Season) {
        selectedSeason.postValue(season)
        val index: Int = seasons.value!!.indexOf(season)
        episodesToShow.postValue(allEpisodes.value!![index])
    }

    private fun makeAPICall(seriesId: Int) {

        IptvRepository.getSeriesInfoBySeriesId(seriesId.toString(), object : SeriesInfoCallback() {
            override fun onSuccess(backendResponse: SeriesInfo) {
                // TODO is postValue assign the value also?
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
            }

            override fun onError(status: Int, message: String) {
                contentName.postValue("Hasan")
            }
        })

    }


}