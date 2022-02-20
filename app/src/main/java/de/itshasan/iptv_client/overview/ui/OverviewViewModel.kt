package de.itshasan.iptv_client.overview.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.SeriesInfoCallback

class OverviewViewModel(seriesId: Int) : ViewModel() {

    var contentName: MutableLiveData<String> = MutableLiveData<String>()
    var releaseDate: MutableLiveData<String> = MutableLiveData<String>()
    var plot: MutableLiveData<String> = MutableLiveData<String>()
    var cast: MutableLiveData<String> = MutableLiveData<String>()
    var director: MutableLiveData<String> = MutableLiveData<String>()


    init {
        makeAPICall(seriesId)
    }

    private fun makeAPICall(seriesId: Int) {

        IptvRepository.getSeriesInfoBySeriesId(seriesId.toString(), object : SeriesInfoCallback() {
            override fun onSuccess(backendResponse: SeriesInfo) {
                contentName.postValue(backendResponse.info.name)
                releaseDate.postValue(backendResponse.info.releaseDate)
                plot.postValue(backendResponse.info.plot)
                cast.postValue(backendResponse.info.cast)
                director.postValue(backendResponse.info.director)
            }

            override fun onError(status: Int, message: String) {
                contentName.postValue("Hasan")
            }
        })

    }


}