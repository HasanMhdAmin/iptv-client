package de.itshasan.iptv_client.dialog.episodesDialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.viewModels
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.databinding.DialogEpisodesBinding
import de.itshasan.iptv_client.controller.contentInfo.adapter.episodes.EpisodeAdapter
import de.itshasan.iptv_client.controller.contentInfo.dialog.SeasonsDialog
import de.itshasan.iptv_client.controller.contentInfo.ui.OverviewViewModel
import de.itshasan.iptv_client.controller.contentInfo.ui.OverviewViewModelFactory
import de.itshasan.iptv_client.utils.navigator.Navigator
import de.itshasan.iptv_core.CoreDialog
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_core.model.series.info.SeriesInfo

private const val TAG = "EpisodesDialog"

class EpisodesDialog(
    private val episode: Episode,
    private val seriesId: Int,
    private val imageUrl: String
) : CoreDialog<DialogEpisodesBinding>(R.layout.dialog_episodes) {

    companion object {
        fun newInstance(episode: Episode, seriesId: Int, imageUrl: String) =
            EpisodesDialog(episode, seriesId, imageUrl)
    }

    private lateinit var viewModel: OverviewViewModel

    override fun provideBinding(layoutInflater: LayoutInflater): DialogEpisodesBinding =
        DialogEpisodesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.back.setOnClickListener { dismiss() }
        binding.seasons.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE

        val viewModel: OverviewViewModel by viewModels { OverviewViewModelFactory(Constant.TYPE_SERIES, seriesId) }
        this.viewModel = viewModel

        this.viewModel.plot.observe(requireActivity()) {
            binding.plotTextView.text = it
            if (it.isEmpty()) binding.plotTextView.visibility = View.GONE
        }

        this.viewModel.episodesToShow.observe(requireActivity()) { episodesList ->
            binding.progressBar.visibility = View.GONE
            val episodeAdapter = EpisodeAdapter(episodes = episodesList, episode)
            binding.episodesRecyclerview.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                adapter = episodeAdapter.apply {
                    onEpisodeClicked = {
                        Navigator.goToSimplePlayer(
                            activity = requireActivity(),
                            target = Constant.TYPE_SERIES,
                            content = it,
                            seriesId = seriesId.toString(),
                            coverUrl = imageUrl,
                            allEpisodes = (viewModel.seriesInfo.value!! as SeriesInfo).exportAllEpisodes()
                                .toMutableList()
                        )
                    }
                }
                val currentPosition =
                    if (episodesList.indexOf(episode) != -1) episodesList.indexOf(episode) else 0
                scrollToPosition(currentPosition)
            }
        }

        this.viewModel.seasons.observe(requireActivity()) { seasonsList ->
            binding.seasons.setOnClickListener {
                if (seasonsList != null) {
                    // TODO put it in navigator class
                    val seasonsDialog = SeasonsDialog.newInstance()
                    seasonsDialog.seasons = seasonsList
                    seasonsDialog.selectedSeason = viewModel.selectedSeason.value!!
                    seasonsDialog.show(
                        parentFragmentManager,
                        TAG
                    )
                    seasonsDialog.onSeasonSelected = {
                        viewModel.setSelectedSeason(it)
                    }
                }
            }
        }

        this.viewModel.selectedSeason.observe(requireActivity()) {
            binding.seasons.visibility = View.VISIBLE
            binding.seasons.text = it.name
        }

    }
}