package de.itshasan.iptv_client.homeScreen.ui.home

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import de.itshasan.iptv_client.category.CategoryActivity
import de.itshasan.iptv_client.continueWatching.adapter.ContinueWatchingAdapter
import de.itshasan.iptv_client.databinding.FragmentHomeBinding
import de.itshasan.iptv_client.login.LoginActivity
import de.itshasan.iptv_client.overview.ui.buttomSheet.ModalBottomSheet
import de.itshasan.iptv_client.utils.navigator.Navigator
import de.itshasan.iptv_core.CoreFragment
import de.itshasan.iptv_core.model.WatchHistory

private const val TAG = "HomeFragment"

class HomeFragment : CoreFragment<FragmentHomeBinding, HomeViewModel>() {

//    val dialog_s = ProgressDialog(
//        requireActivity()
//    )

    lateinit var dialog: ProgressDialog

    override fun provideBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentHomeBinding.inflate(layoutInflater, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this)[HomeViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.series.setOnClickListener {
            val intent = Intent(requireActivity(), CategoryActivity::class.java)
            startActivity(intent)
        }

        binding.live.setOnClickListener {
            val intent = Intent(requireActivity(), LoginActivity::class.java)
            startActivity(intent)
        }

        observers()
    }

    override fun onResume() {
        super.onResume()
        viewMode.getContinueWatch()
    }

    private fun observers() {

        viewMode.history.observe(requireActivity()) { history ->
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

                        viewMode.getSeriesInfoBySeriesId(it)

                    }
                    onWatchHistoryLongClicked = {
                        showBottomSheetDialog(it)
                    }
                }
            }
        }

        viewMode.episodeWatchHistory.observe(requireActivity()) {
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
            viewMode.getContinueWatch()
        }
        bottomSheetDialog.show(parentFragmentManager, bottomSheetDialog.tag)
    }

}