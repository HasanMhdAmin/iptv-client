package de.itshasan.iptv_client.seriesList.ui.main

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class GalleryViewModelFactory(private val categoryID: String, val app: Application) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return GalleryViewModel(categoryID, app) as T
    }
}