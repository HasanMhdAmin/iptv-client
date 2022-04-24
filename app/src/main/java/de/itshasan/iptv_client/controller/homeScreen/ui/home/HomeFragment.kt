package de.itshasan.iptv_client.controller.homeScreen.ui.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.controller.contentInfo.ui.buttomSheet.ModalBottomSheet
import de.itshasan.iptv_client.controller.continueWatching.adapter.ContinueWatchingAdapter
import de.itshasan.iptv_client.controller.gallery.ui.main.GalleryViewModel
//import de.itshasan.iptv_client.controller.gallery.ui.main.GalleryViewModelFactory
import de.itshasan.iptv_client.controller.login.LoginActivity
import de.itshasan.iptv_client.databinding.FragmentHomeBinding
import de.itshasan.iptv_client.utils.navigator.Navigator
import de.itshasan.iptv_core.CoreFragment
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.WatchHistory

private const val TAG = "HomeFragment"

class HomeFragment : CoreFragment<FragmentHomeBinding, HomeViewModel>() {

    lateinit var dialog: ProgressDialog

    override fun provideBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(layoutInflater, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this)[HomeViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.movies.setOnClickListener {
            val bundle = bundleOf(
                Constant.TARGET to Constant.TYPE_MOVIES,
                Constant.CATEGORY_ID to Constant.ALL_MOVIES,
            )
            findNavController().navigate(
                R.id.action_navigation_home_to_navigation_gallery,
                bundle
            )
        }

        binding.series.setOnClickListener {
            val categoryId = Constant.ALL_SERIES
            val bundle = bundleOf(
                Constant.TARGET to Constant.TYPE_SERIES,
                Constant.CATEGORY_ID to categoryId,
            )
            findNavController().navigate(
                R.id.action_navigation_home_to_navigation_gallery,
                bundle
            )
        }

        binding.live.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        binding.refreshLayout.setOnRefreshListener {

            // Update continue watching
            viewModel.getContinueWatch()

            // Update series list
            val galleryViewModel = ViewModelProvider(this)[GalleryViewModel::class.java]
            galleryViewModel.getSeries(Constant.ALL_SERIES)

        }

        Log.d(TAG, "onViewCreated: ")
        observers()
    }

    override fun onResume() {
        super.onResume()
        viewModel.getContinueWatch()
    }

    private fun observers() {

        viewModel.history.observe(requireActivity()) { history ->
            binding.refreshLayout.isRefreshing = false
            val continueWatchingAdapter = ContinueWatchingAdapter(history)

            binding.continueWatchingRecyclerView.apply {
                layoutManager = LinearLayoutManager(
                    requireActivity(), LinearLayoutManager.HORIZONTAL, false
                )
                adapter = continueWatchingAdapter.apply {
                    onWatchHistoryClicked = {
                        dialog = ProgressDialog.show(
                            context,
                            "Fetching content info",
                            "preparing the content, Please wait...",
                            true
                        )
                        if (it.contentType == Constant.TYPE_SERIES)
                            viewModel.getSeriesInfoBySeriesId(it)
                        else if (it.contentType == Constant.TYPE_MOVIES)
                            viewModel.getMovieInfo(it)

                    }
                    onWatchHistoryLongClicked = {
                        showBottomSheetDialog(it)
                    }
                }
            }
        }

        viewModel.contentWatchHistory.observe(requireActivity()) {
            if (it.content != null) {
                dialog.dismiss()
                Log.d(TAG, "observers: episodeWatchHistory")

                Navigator.goToSimplePlayer(
                    requireActivity(),
                    target = it.watchHistory.contentType,
                    content = it.content!!,
                    seriesId = it.watchHistory.parentId,
                    coverUrl = it.watchHistory.coverUrl,
                    it.episodeList.toMutableList()
                )
                it.content = null
            }
        }

    }

    private fun showBottomSheetDialog(watchHistory: WatchHistory) {
        val bottomSheetDialog = ModalBottomSheet(viewModel, watchHistory)
        bottomSheetDialog.onItemRemovedCallback = {
            viewModel.getContinueWatch()
        }
        bottomSheetDialog.show(parentFragmentManager, bottomSheetDialog.tag)
    }

}