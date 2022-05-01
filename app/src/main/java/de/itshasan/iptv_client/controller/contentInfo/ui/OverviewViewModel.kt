package de.itshasan.iptv_client.controller.contentInfo.ui

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.itshasan.iptv_client.utils.firebase.Firestore
import de.itshasan.iptv_core.model.*
import de.itshasan.iptv_core.model.movie.MovieInfo
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_core.model.series.info.season.Season
import de.itshasan.iptv_core.storage.LocalStorage
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_network.network.IptvNetwork
import de.itshasan.iptv_network.network.callback.MovieInfoCallback
import de.itshasan.iptv_network.network.callback.SeriesInfoCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

private val TAG = OverviewViewModel::class.java.simpleName

class OverviewViewModel(private val target: String, contentId: Int) : ViewModel() {

    var seriesInfo: MutableLiveData<ContentInfo> = MutableLiveData<ContentInfo>()
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
    var currentContentProgress = MutableLiveData<Pair<Posterable, WatchHistory?>>()
    var movieInfo = MutableLiveData<MovieInfo?>()
    var isFav = MutableLiveData<Boolean>()

    init {

        isInFav(
            LocalStorage.getUniqueContentId(contentId.toString(), target)
        )

        if (target == Constant.TYPE_SERIES)
            makeAPICall(contentId)
        else if (target == Constant.TYPE_MOVIES)
            getMovieInfo(contentId)
    }

    fun setSelectedSeason(season: Season) {
        selectedSeason.postValue(season)
        val index: Int = seasons.value!!.indexOf(season)
        episodesToShow.postValue(allEpisodes.value!![index])
    }


    fun updateWatchHistory(contentId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val watchHistory =
                iptvDatabase.watchHistoryDao()
                    .getSeriesByParentIdOrderByTimestamp(contentId.toString()).firstOrNull()
            if (watchHistory != null) {
                Log.d(TAG, "onSuccess: watchHistory != null")
                if (movieInfo.value != null)
                    currentContentProgress.postValue(Pair(movieInfo.value?.movie!!, watchHistory))

                run breaking@{
                    allEpisodes.value?.forEachIndexed { index, list ->
                        val item = list.find { item -> item.id == watchHistory.contentId }
                        if (item != null) {
                            Log.d(TAG, "onSuccess: item != null")
                            currentContentProgress.postValue(Pair(item, watchHistory))
                            setSelectedSeason(seasons.value!![index])
                            return@breaking
                        } else {
                            Log.d(TAG, "onSuccess: item == null")
                        }
                    }
                }
            } else {
                Log.d(TAG, "onSuccess: watchHistory == null")
                if (target == Constant.TYPE_SERIES)
                    currentContentProgress.postValue(Pair(allEpisodes.value!![0][0], null))
                else if (target == Constant.TYPE_MOVIES)
                    currentContentProgress.postValue(Pair(movieInfo.value!!.movie, null))

            }
        }
    }

    private fun makeAPICall(seriesId: Int) {

        IptvNetwork.getSeriesInfoBySeriesId(seriesId.toString(), object : SeriesInfoCallback() {
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

    private fun getMovieInfo(movieId: Int) {
        IptvNetwork.getMovieInfo(movieId.toString(), object : MovieInfoCallback() {
            override fun onSuccess(backendResponse: MovieInfo) {
                seriesInfo.postValue(backendResponse)
                contentName.postValue(backendResponse.movie.name)
                releaseDate.postValue(backendResponse.info.releasedate)
                plot.postValue(backendResponse.info.plot)
                cast.postValue(backendResponse.info.cast)
                director.postValue(backendResponse.info.director)
                coverImageUrl.postValue(backendResponse.info.movieImage)

                movieInfo.value = backendResponse
                updateWatchHistory(movieId)
            }

            override fun onError(status: Int, message: String) {
                contentName.postValue("Hasan")
            }
        })
    }

    private fun isInFav(uniqueContentId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val fav = iptvDatabase.favouriteDao().getFavouriteItem(uniqueContentId)
            isFav.postValue(fav != null)
        }
    }

    fun changeFav(favourite: Favourite) {

        viewModelScope.launch(Dispatchers.IO) {
            val fav = iptvDatabase.favouriteDao().getFavouriteItem(favourite.uniqueId)
            if (fav != null) {
                // existed
                iptvDatabase.favouriteDao().delete(fav)
                isFav.postValue(false)
                Firestore.favoriteDao().removeFavoriteToFirestore(favourite)
            } else {
                iptvDatabase.favouriteDao().insert(favourite)
                isFav.postValue(true)
                Firestore.favoriteDao().addFavoriteToFirestore(favourite)
            }
        }


    }
}