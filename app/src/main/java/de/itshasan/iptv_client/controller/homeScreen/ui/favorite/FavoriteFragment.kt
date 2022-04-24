package de.itshasan.iptv_client.controller.homeScreen.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import de.itshasan.iptv_client.databinding.FragmentFavoriteBinding
import de.itshasan.iptv_core.CoreFragment

class FavoriteFragment : CoreFragment<FragmentFavoriteBinding, FavoriteViewModel>() {

    override fun provideBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFavoriteBinding.inflate(layoutInflater, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this)[FavoriteViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.text.observe(viewLifecycleOwner) {
            binding.textNotifications.text = it
        }

    }

}