package de.itshasan.iptv_client.overview.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.overview.adapter.episodes.EpisodeAdapter
import de.itshasan.iptv_client.overview.dialog.SeasonsDialog
import de.itshasan.iptv_core.model.Constant

private val TAG = OverviewFragment::class.java.simpleName

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.overview_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val nameTextView = view.findViewById<TextView>(R.id.nameTextView)
        val releaseDateTextView = view.findViewById<TextView>(R.id.releaseDateTextView)
        val plotTextView = view.findViewById<TextView>(R.id.plotTextView)
        val castTextView = view.findViewById<TextView>(R.id.castTextView)
        val directorTextView = view.findViewById<TextView>(R.id.directorTextView)
        val coverImageView = view.findViewById<ImageView>(R.id.coverImageView)
        val seasons = view.findViewById<Button>(R.id.seasons)
        val episodesRecyclerview = view.findViewById<RecyclerView>(R.id.episodesRecyclerview)

        val seriesId = activity?.intent?.extras?.getInt(Constant.SERIES_ID, 0)

        val imageUrl = activity?.intent?.extras?.getString("IMAGE_URL")
        val title = activity?.intent?.extras?.getString("SERIES_TITLE")
        Log.d(TAG, "onViewCreated: seriesId: $seriesId")
        val viewModel: OverviewViewModel by viewModels { OverviewViewModelFactory(seriesId!!) }
        this.viewModel = viewModel

        this.viewModel.contentName.observe(requireActivity()) {
            nameTextView.text = it
        }
        this.viewModel.releaseDate.observe(requireActivity()) {
            releaseDateTextView.text = it
            if (it.isEmpty()) releaseDateTextView.visibility = View.GONE
        }
        this.viewModel.plot.observe(requireActivity()) {
            plotTextView.text = it
            if (it.isEmpty()) plotTextView.visibility = View.GONE
        }
        this.viewModel.cast.observe(requireActivity()) {
            castTextView.text = it
            if (it.isEmpty()) castTextView.visibility = View.GONE
        }
        this.viewModel.director.observe(requireActivity()) {
            directorTextView.text = it
            if (it.isEmpty()) directorTextView.visibility = View.GONE
        }
        this.viewModel.seasons.observe(requireActivity()) { seasonsList ->
            Log.d(TAG, "onViewCreated: seasons: ${seasonsList.size}")
            seasons.setOnClickListener {
                if (seasonsList != null) {
                    val seasonsDialog = SeasonsDialog.newInstance()
                    seasonsDialog.seasons = seasonsList
                    seasonsDialog.selectedSeason = viewModel.selectedSeason.value!!
                    seasonsDialog.show(parentFragmentManager, TAG)
                    seasonsDialog.onSeasonSelected = {
                        viewModel.setSelectedSeason(it)
                    }
                }
            }
        }

        this.viewModel.selectedSeason.observe(requireActivity()) {
            seasons.text = it.name
        }

        this.viewModel.episodesToShow.observe(requireActivity()) { episodesList ->
            val episodeAdapter = EpisodeAdapter(episodes = episodesList)
            episodesRecyclerview.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                adapter = episodeAdapter.apply {
                    onEpisodeClicked = {

                    }
                }
            }
        }
//        this.viewModel.coverImageUrl.observe(requireActivity()) {
//            Glide
//                .with(view.context)
//                .load(it)
//                .centerCrop()
//                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                .into(coverImageView)
//
//        }

        nameTextView.text = title

        Glide
            .with(view.context)
            .load(imageUrl)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(coverImageView)


    }

}