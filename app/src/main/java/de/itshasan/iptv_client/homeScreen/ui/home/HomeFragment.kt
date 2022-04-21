package de.itshasan.iptv_client.homeScreen.ui.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.continueWatching.adapter.ContinueWatchingAdapter
import de.itshasan.iptv_client.databinding.FragmentHomeBinding
import de.itshasan.iptv_client.login.LoginActivity
import de.itshasan.iptv_client.overview.ui.buttomSheet.ModalBottomSheet
import de.itshasan.iptv_client.seriesList.ui.main.GalleryViewModel
import de.itshasan.iptv_client.seriesList.ui.main.GalleryViewModelFactory
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
            val viewModel: GalleryViewModel by viewModels {
                GalleryViewModelFactory(Constant.ALL_SERIES, requireActivity().application)
            }
            viewModel.getSeries(Constant.ALL_SERIES)

        }

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
                        viewModel.getSeriesInfoBySeriesId(it)

                    }
                    onWatchHistoryLongClicked = {
                        showBottomSheetDialog(it)
                    }
                }
            }
        }

        viewModel.episodeWatchHistory.observe(requireActivity()) {
            if (it.episode != null) {
                dialog.dismiss()

                Navigator.goToSimplePlayer(
                    requireActivity(),
                    episode = it.episode,
                    seriesId = it.watchHistory.parentId,
                    coverUrl = it.watchHistory.coverUrl,
                    it.episodeList
                )
            }
        }

    }

    private fun showBottomSheetDialog(watchHistory: WatchHistory) {
        val bottomSheetDialog = ModalBottomSheet(watchHistory)
        bottomSheetDialog.onItemRemovedCallback = {
            viewModel.getContinueWatch()
        }
        bottomSheetDialog.show(parentFragmentManager, bottomSheetDialog.tag)
    }

}