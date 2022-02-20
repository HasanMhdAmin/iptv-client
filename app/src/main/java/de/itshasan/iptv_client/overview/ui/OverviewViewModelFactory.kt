package de.itshasan.iptv_client.overview.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class OverviewViewModelFactory(private val seriesId: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OverviewViewModel(seriesId) as T
    }
}