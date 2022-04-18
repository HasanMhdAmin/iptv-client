package de.itshasan.iptv_client.homeScreen.ui.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.itshasan.iptv_client.utils.firebase.Firestore
import de.itshasan.iptv_core.model.WatchHistory
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_core.utils.Serializer
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.SeriesInfoCallback
import de.itshasan.iptv_repository.storage.LocalStorage
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HomeViewModel : ViewModel() {

    var history: MutableLiveData<List<WatchHistory>> = MutableLiveData<List<WatchHistory>>()
    var episodeWatchHistory: MutableLiveData<Item> = MutableLiveData<Item>()

    fun getContinueWatch() {

        viewModelScope.launch(Dispatchers.IO) {
            // Pull db updates
            Firestore.firestore.collection("users_test")
                .whereEqualTo("userId", LocalStorage.getUniqueUserId()).get()
                .addOnSuccessListener { result ->
                    viewModelScope.launch(Dispatchers.IO) {
                        for (document in result) {
                            val jsonString = Serializer.appGson.toJson(document.data)
                            val watchHistory =
                                Serializer.appGson.fromJson(jsonString, WatchHistory::class.java)
                            iptvDatabase.watchHistoryDao().insert(watchHistory)
                        }

                        history.postValue(
                            iptvDatabase.watchHistoryDao().getContinueWatching()
                        )
                    }

                    // Push db updates
                    history.value?.forEachIndexed { index, it ->
                        Firestore.firestore.collection("users_test").document(it.uniqueId).set(it)
                    }

                }
        }

    }

    fun getSeriesInfoBySeriesId(watchHistory: WatchHistory) {
        IptvRepository.getSeriesInfoBySeriesId(
            watchHistory.parentId,
            object : SeriesInfoCallback() {
                override fun onSuccess(backendResponse: SeriesInfo) {
                    val episodes = backendResponse.episodes
                    var currentEpisode: Episode? = null
                    for (season in episodes) {
                        for (ep in season) {
                            if (ep.id == watchHistory.contentId) {
                                currentEpisode = ep
                                break
                            }
                        }
                    }
                    episodeWatchHistory.postValue(
                        Item(
                            currentEpisode,
                            watchHistory,
                            backendResponse.exportAllEpisodes()
                        )
                    )
                }

                override fun onError(status: Int, message: String) {
                }
            })
    }

}

data class Item(
    val episode: Episode? = null,
    val watchHistory: WatchHistory,
    val episodeList: List<Episode>
)
