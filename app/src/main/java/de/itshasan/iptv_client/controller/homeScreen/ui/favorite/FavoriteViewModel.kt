package de.itshasan.iptv_client.controller.homeScreen.ui.favorite

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.itshasan.iptv_client.controller.gallery.adapter.gallery.GalleryAdapter
import de.itshasan.iptv_core.model.Favourite
import de.itshasan.iptv_core.model.Posterable
import de.itshasan.iptv_database.database.iptvDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FavoriteViewModel : ViewModel() {

    var favorites: MutableLiveData<List<Favourite>> = MutableLiveData<List<Favourite>>()
    var galleryRecyclerViewAdapter: GalleryAdapter<Posterable> = GalleryAdapter<Posterable>()

    init {
        getFavorite()
    }

    fun getAdapter(): GalleryAdapter<Posterable> {
        return galleryRecyclerViewAdapter
    }

    fun setAdapterData(data: ArrayList<Posterable>) {
        galleryRecyclerViewAdapter.setDataList(data)
    }

    private fun getFavorite() {
        viewModelScope.launch(Dispatchers.IO) {
            val fav = iptvDatabase.favouriteDao().getAll()
            favorites.postValue(fav)
        }

    }
}