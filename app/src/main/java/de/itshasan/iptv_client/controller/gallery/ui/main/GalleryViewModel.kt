package de.itshasan.iptv_client.controller.gallery.ui.main

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.itshasan.iptv_client.controller.gallery.adapter.gallery.GalleryAdapter
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.Posterable
import de.itshasan.iptv_core.model.movie.Movie
import de.itshasan.iptv_core.model.series.SeriesList
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_network.network.IptvNetwork
import de.itshasan.iptv_network.network.callback.MoviesCallback
import de.itshasan.iptv_network.network.callback.SeriesCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val TAG = GalleryViewModel::class.java.simpleName

class GalleryViewModel : ViewModel() {

    var recyclerListData: MutableLiveData<ArrayList<Posterable>?> =
        MutableLiveData<ArrayList<Posterable>?>()
    var musicRecyclerViewAdapter: GalleryAdapter<Posterable> = GalleryAdapter<Posterable>()


    fun getAdapter(): GalleryAdapter<Posterable> {
        return musicRecyclerViewAdapter
    }

    fun setAdapterData(data: ArrayList<Posterable>) {
        musicRecyclerViewAdapter.setDataList(data)
        musicRecyclerViewAdapter.notifyDataSetChanged()
    }

    fun getRecyclerListDataObserver(): MutableLiveData<ArrayList<Posterable>?> {
        return recyclerListData
    }

    fun getSeries(categoryId: String) {

        IptvNetwork.getSeriesByCategoryId(categoryId, object : SeriesCallback() {
            override fun onSuccess(backendResponse: SeriesList) {
                recyclerListData.postValue(backendResponse as ArrayList<Posterable>)

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

    fun getMovies() {

        IptvNetwork.getMovies(object : MoviesCallback() {
            override fun onSuccess(backendResponse: ArrayList<Movie>) {
                recyclerListData.postValue(backendResponse as ArrayList<Posterable>)

                // Cache to database
                viewModelScope.launch(Dispatchers.IO) {
                    iptvDatabase.moviesDao().insertAll(backendResponse)
                }
            }

            override fun onError(status: Int, message: String) {
                recyclerListData.postValue(null)
            }

        })


    }


    fun setCategory(target: String, categoryId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            if (target == Constant.TYPE_SERIES) {
                val series = if (categoryId == Constant.ALL_SERIES)
                    iptvDatabase.seriesItemDao().getAll()
                else
                    iptvDatabase.seriesItemDao().getSeriesByCategoryId(categoryId)
                if (series.isEmpty()) {
                    getSeries(categoryId)
                } else {
                    val seriesList = SeriesList()
                    seriesList.addAll(series)
                    recyclerListData.postValue(seriesList as ArrayList<Posterable>)
                }
            } else {
                val movies = if (categoryId == Constant.ALL_MOVIES)
                    iptvDatabase.moviesDao().getAll()
                else
                    iptvDatabase.moviesDao().getMoviesByCategoryId(categoryId)
                if (movies.isEmpty()) {
                    getMovies()
                } else {
//                    val seriesList = SeriesList()
//                    seriesList.addAll(series)
                    recyclerListData.postValue(movies as ArrayList<Posterable>)
                }
            }


        }
    }


}