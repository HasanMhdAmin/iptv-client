package de.itshasan.iptv_client.homeScreen.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import de.itshasan.iptv_client.databinding.FragmentHomeBinding
import de.itshasan.iptv_core.CoreFragment

class HomeFragment : CoreFragment<FragmentHomeBinding, HomeViewModel>() {

    override fun provideBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(layoutInflater, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this)[HomeViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewMode.text.observe(viewLifecycleOwner) {
            binding.textHome.text = it
        }

    }

}