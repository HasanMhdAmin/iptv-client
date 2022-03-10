package de.itshasan.iptv_client.overview.ui

import android.content.Intent
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
import com.google.gson.Gson
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.SimplePlayerActivity
import de.itshasan.iptv_client.overview.adapter.episodes.EpisodeAdapter
import de.itshasan.iptv_client.overview.dialog.SeasonsDialog
import de.itshasan.iptv_core.model.Constant


private val TAG = OverviewFragment::class.java.simpleName

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel
    private var seriesId: Int = 0

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

        seriesId = activity?.intent?.extras?.getInt(Constant.SERIES_ID, 0)!!

        val imageUrl = activity?.intent?.extras?.getString("IMAGE_URL")
        val title = activity?.intent?.extras?.getString("SERIES_TITLE")

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

                        val episodeUrl = "http://teslaiptv.com:8080/series/hasanxmhdxamin/569247364/${it.id}.${it.containerExtension}"
                        val intent =
                            Intent(context, SimplePlayerActivity::class.java).apply {
                                val gson = Gson()
                                val serializedEpisode = gson.toJson(it)
                                putExtra(Constant.CONTENT, serializedEpisode)
                                Log.d(TAG, "onViewCreated: seriesId : $seriesId")
                                putExtra(Constant.SERIES_ID, seriesId.toString())
                            }
                        startActivity(intent)
                    }
                }
            }
        }

        nameTextView.text = title

        Glide
            .with(view.context)
            .load(imageUrl)
            .centerCrop()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(coverImageView)


    }

}