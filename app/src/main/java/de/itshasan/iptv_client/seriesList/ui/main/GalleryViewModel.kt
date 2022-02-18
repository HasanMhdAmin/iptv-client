package de.itshasan.iptv_client.seriesList.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import de.itshasan.iptv_client.seriesList.adapter.GalleryAdapter
import de.itshasan.iptv_core.model.series.SeriesList
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.SeriesCallback

class GalleryViewModel : ViewModel() {

    var recyclerListData: MutableLiveData<SeriesList> = MutableLiveData<SeriesList>()
    var musicRecyclerViewAdapter: GalleryAdapter = GalleryAdapter()

    init {
        makeAPICall()
    }

    fun getAdapter(): GalleryAdapter {
        return musicRecyclerViewAdapter
    }

    fun setAdapterData(data: SeriesList) {
        musicRecyclerViewAdapter.setDataList(data)
        musicRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun getRecyclerListDataObserver(): MutableLiveData<SeriesList> {
        return recyclerListData
    }

    private fun makeAPICall() {

        IptvRepository.getSeriesByCategoryId("137", object : SeriesCallback() {
            override fun onSuccess(backendResponse: SeriesList) {
                recyclerListData.postValue(backendResponse)
            }

            override fun onError(status: Int, message: String) {
                recyclerListData.postValue(null)
            }
        })

    }


}