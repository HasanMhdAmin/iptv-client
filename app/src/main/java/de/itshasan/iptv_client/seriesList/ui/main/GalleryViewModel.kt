package de.itshasan.iptv_client.seriesList.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import de.itshasan.iptv_client.seriesList.adapter.gallery.GalleryAdapter
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.series.SeriesList
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_network.network.IptvNetwork
import de.itshasan.iptv_network.network.callback.SeriesCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val TAG = GalleryViewModel::class.java.simpleName

class GalleryViewModel(categoryId: String, application: Application) :
    AndroidViewModel(application) {

    var recyclerListData: MutableLiveData<SeriesList?> = MutableLiveData<SeriesList?>()
    var musicRecyclerViewAdapter: GalleryAdapter = GalleryAdapter()


    fun getAdapter(): GalleryAdapter {
        return musicRecyclerViewAdapter
    }

    fun setAdapterData(data: SeriesList) {
        musicRecyclerViewAdapter.setDataList(data)
        musicRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun getRecyclerListDataObserver(): MutableLiveData<SeriesList?> {
        return recyclerListData
    }

    fun getSeries(categoryId: String) {

        IptvNetwork.getSeriesByCategoryId(categoryId, object : SeriesCallback() {
            override fun onSuccess(backendResponse: SeriesList) {
                recyclerListData.postValue(backendResponse)

                // Cache to database
                viewModelScope.launch(Dispatchers.IO) {
                    iptvDatabase.seriesItemDao().insertAll(backendResponse)
                }
            }

            override fun onError(status: Int, message: String) {
                recyclerListData.postValue(null)
            }
        })

    }

    fun setCategory(categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {

            val series = if (categoryId == Constant.ALL_SERIES)
                iptvDatabase.seriesItemDao().getAll()
            else
                iptvDatabase.seriesItemDao().getSeriesByCategoryId(categoryId)
            if (series.isEmpty()) {
                getSeries(categoryId)
            } else {
                val seriesList = SeriesList()
                seriesList.addAll(series)
                recyclerListData.postValue(seriesList)
            }

        }
    }


}