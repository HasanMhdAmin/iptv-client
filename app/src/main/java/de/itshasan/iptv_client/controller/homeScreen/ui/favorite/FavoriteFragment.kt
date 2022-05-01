package de.itshasan.iptv_client.controller.homeScreen.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.databinding.FragmentFavoriteBinding
import de.itshasan.iptv_core.CoreFragment
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.Favourite
import de.itshasan.iptv_core.model.Posterable

class FavoriteFragment : CoreFragment<FragmentFavoriteBinding, FavoriteViewModel>() {

    override fun provideBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentFavoriteBinding.inflate(layoutInflater, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this)[FavoriteViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.favorites.observe(requireActivity()) {

            binding.favoriteRecyclerView.apply {
                layoutManager = GridLayoutManager(context, 3)
                adapter = viewModel.getAdapter().apply {
                    viewModel.setAdapterData(ArrayList(it))
                    onItemClicked =
                        { posterable: Posterable, imageView: ImageView, textview: TextView ->

                            val extras = FragmentNavigatorExtras(
                                imageView to posterable.getPosterUrl(),
                                textview to posterable.getTitle()
                            )

                            val bundle = bundleOf(
                                Constant.TARGET to (posterable as Favourite).contentType,
                                Constant.CONTENT_ID to posterable.getId(),
                                Constant.COVER_URL to posterable.getPosterUrl(),
                                Constant.SERIES_TITLE to posterable.getTitle()
                            )

                            findNavController().navigate(
                                R.id.action_navigation_favorite_to_navigation_details,
                                bundle,
                                null,
                                extras
                            )
                        }
                }
            }


        }
    }

}