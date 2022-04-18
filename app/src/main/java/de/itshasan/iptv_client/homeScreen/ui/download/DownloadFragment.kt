package de.itshasan.iptv_client.homeScreen.ui.download

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import de.itshasan.iptv_client.databinding.FragmentDownloadBinding
import de.itshasan.iptv_core.CoreFragment

class DownloadFragment : CoreFragment<FragmentDownloadBinding, DownloadViewModel>() {

    override fun provideBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDownloadBinding.inflate(layoutInflater, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this)[DownloadViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.text.observe(viewLifecycleOwner) {
            binding.textDashboard.text = it
        }

    }

}