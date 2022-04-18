package de.itshasan.iptv_client.homeScreen.ui.download

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DownloadViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Coming soon..."
    }
    val text: LiveData<String> = _text
}