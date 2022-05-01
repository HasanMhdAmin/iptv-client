package de.itshasan.iptv_client.controller.contentInfo.ui

import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.controller.contentInfo.adapter.episodes.EpisodeAdapter
import de.itshasan.iptv_client.controller.contentInfo.dialog.SeasonsDialog
import de.itshasan.iptv_client.utils.navigator.Navigator
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.Favourite
import de.itshasan.iptv_core.model.Posterable
import de.itshasan.iptv_core.model.series.info.SeriesInfo
import de.itshasan.iptv_core.storage.LocalStorage

private val TAG = OverviewFragment::class.java.simpleName

class OverviewFragment : Fragment() {

    companion object {
        fun newInstance() = OverviewFragment()
    }

    private lateinit var viewModel: OverviewViewModel
    private var contentId: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = TransitionInflater.from(requireActivity())
            .inflateTransition(android.R.transition.move)

    }

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
        val seasonCount = view.findViewById<TextView>(R.id.seasonCount)
        val titleTextView = view.findViewById<TextView>(R.id.title)
        val play = view.findViewById<Button>(R.id.play)
        val fav = view.findViewById<LinearLayout>(R.id.fav)
        val favIcon = view.findViewById<ImageView>(R.id.favIcon)
        val progress = view.findViewById<ProgressBar>(R.id.progress)
        val plotTextView = view.findViewById<TextView>(R.id.plotTextView)
        val castTextView = view.findViewById<TextView>(R.id.castTextView)
        val directorTextView = view.findViewById<TextView>(R.id.directorTextView)
        val coverImageView = view.findViewById<ImageView>(R.id.coverImageView)
        val seasons = view.findViewById<Button>(R.id.seasons)
        val episodesRecyclerview = view.findViewById<RecyclerView>(R.id.episodesRecyclerview)

        contentId = arguments?.getInt(Constant.CONTENT_ID, 0)!!
        val target = arguments?.getString(Constant.TARGET, Constant.TYPE_MOVIES)!!
        val imageUrl = arguments?.getString(Constant.COVER_URL)
        val title = arguments?.getString(Constant.SERIES_TITLE)

        val viewModel: OverviewViewModel by viewModels {
            OverviewViewModelFactory(
                target,
                contentId
            )
        }
        this.viewModel = viewModel

        if (target == Constant.TYPE_MOVIES)
            seasons.visibility = View.GONE

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
            castTextView.text = Html.fromHtml(
                resources.getString(R.string.cast_text_test, it),
                Html.FROM_HTML_MODE_COMPACT
            )
            if (it.isEmpty()) castTextView.visibility = View.GONE
        }
        this.viewModel.director.observe(requireActivity()) {
            directorTextView.text =
                Html.fromHtml(
                    resources.getString(R.string.director_text_test, it),
                    Html.FROM_HTML_MODE_COMPACT
                )
            if (it.isEmpty()) directorTextView.visibility = View.GONE
        }
        this.viewModel.seasons.observe(requireActivity()) { seasonsList ->

            seasonCount.text = if (seasonsList.size == 1 || seasonsList.isEmpty()) {
                getString(R.string.one_season)
            } else {
                "${seasonsList.size} Seasons"
            }

            seasons.setOnClickListener {
                if (seasonsList != null) {
                    // TODO put it in navigator class
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

        viewModel.currentContentProgress.observe(requireActivity()) {
            var startTimestamp = 0L
            if (it.second != null) {
                titleTextView.text = it.first.getTitle()
                startTimestamp = it.second!!.currentTime
                val progressValue = 100 * startTimestamp / it.second!!.totalTime
                progress.setProgress(progressValue.toInt(), true)
            }

            play.setOnClickListener { _ ->
                var allEpisodes = emptyList<Posterable>()
                if (target == Constant.TYPE_SERIES) {
                    allEpisodes = (viewModel.seriesInfo.value!! as SeriesInfo).exportAllEpisodes()
                } else if (target == Constant.TYPE_MOVIES) {

                }
                Navigator.goToSimplePlayer(
                    activity = requireActivity(),
                    target = target,
                    content = it.first,
                    seriesId = contentId.toString(),
                    coverUrl = imageUrl,
                    allEpisodes = allEpisodes.toMutableList()
                )
            }
        }

//        this.viewModel.movieInfo.observe(requireActivity()) {
//
//        }

        this.viewModel.episodesToShow.observe(requireActivity()) { episodesList ->
            val episodeAdapter = EpisodeAdapter(episodes = episodesList)
            episodesRecyclerview.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                adapter = episodeAdapter.apply {
                    onEpisodeClicked = {
                        Navigator.goToSimplePlayer(
                            activity = requireActivity(),
                            content = it,
                            target = target,
                            seriesId = contentId.toString(),
                            coverUrl = imageUrl,
                            allEpisodes = (viewModel.seriesInfo.value!! as SeriesInfo).exportAllEpisodes()
                                .toMutableList()
                        )
                    }
                }
            }
        }

        this.viewModel.isFav.observe(requireActivity()) {
            var drawable = AppCompatResources.getDrawable(
                requireContext(),
                R.drawable.ic_favorite_off
            )
            if (it) {
                drawable = AppCompatResources.getDrawable(
                    requireContext(),
                    R.drawable.ic_favorite
                )
            }
            favIcon.setImageDrawable(drawable)
        }

        nameTextView.apply {
            transitionName = title
            text = title
        }

        coverImageView.apply {
            transitionName = imageUrl
            Glide
                .with(view.context)
                .load(imageUrl)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(coverImageView)
        }

        fav.setOnClickListener {
            viewModel.changeFav(
                Favourite(
                    favId = 0,
                    parentId = contentId.toString(),
                    name = title,
                    contentType = target,
                    timestamp = System.currentTimeMillis(),
                    coverUrl = imageUrl,
                    uniqueId = LocalStorage.getUniqueContentId(id = contentId.toString(), target),
                    userId = LocalStorage.getUniqueUserId()
                )
            )

        }

    }

    override fun onResume() {
        super.onResume()
        if (viewModel.allEpisodes.value != null)
            viewModel.updateWatchHistory(contentId)
    }

}