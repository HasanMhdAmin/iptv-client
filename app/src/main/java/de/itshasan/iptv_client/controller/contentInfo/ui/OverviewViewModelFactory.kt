package de.itshasan.iptv_client.controller.contentInfo.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider


class OverviewViewModelFactory(private val target: String, private val contentId: Int) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return OverviewViewModel(target, contentId) as T
    }
}