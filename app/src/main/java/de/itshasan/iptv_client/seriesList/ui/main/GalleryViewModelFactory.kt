package de.itshasan.iptv_client.seriesList.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class GalleryViewModelFactory(private val categoryID: String) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(categoryID) as T
    }
}