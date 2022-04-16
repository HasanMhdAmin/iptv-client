package de.itshasan.iptv_client.episodesDialog

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.databinding.DialogEpisodesBinding
import de.itshasan.iptv_client.overview.adapter.episodes.EpisodeAdapter
import de.itshasan.iptv_client.overview.ui.OverviewViewModel
import de.itshasan.iptv_client.overview.ui.OverviewViewModelFactory
import de.itshasan.iptv_client.utils.navigator.Navigator
import de.itshasan.iptv_core.CoreDialog

private const val TAG = "EpisodesDialog"

class EpisodesDialog(
    private val seriesId: Int,
    private val imageUrl: String
) : CoreDialog<DialogEpisodesBinding>(R.layout.dialog_episodes) {

    companion object {
        fun newInstance(seriesId: Int, imageUrl: String) = EpisodesDialog(seriesId, imageUrl)
    }

    private lateinit var viewModel: OverviewViewModel

    override fun provideBinding(layoutInflater: LayoutInflater): DialogEpisodesBinding =
        DialogEpisodesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener { dismiss() }

        val viewModel: OverviewViewModel by viewModels { OverviewViewModelFactory(seriesId) }
        this.viewModel = viewModel

        this.viewModel.episodesToShow.observe(requireActivity()) { episodesList ->
            Log.d(TAG, "onViewCreated: episodesList : ${episodesList.size}")
            val episodeAdapter = EpisodeAdapter(episodes = episodesList)
            binding.episodesRecyclerview.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                adapter = episodeAdapter.apply {
                    onEpisodeClicked = {
                        Navigator.goToSimplePlayer(
                            activity = requireActivity(),
                            episode = it,
                            seriesId = seriesId.toString(),
                            coverUrl = imageUrl,
                            allEpisodes = viewModel.seriesInfo.value!!.exportAllEpisodes()
                        )
                    }
                }
            }
        }
    }
}