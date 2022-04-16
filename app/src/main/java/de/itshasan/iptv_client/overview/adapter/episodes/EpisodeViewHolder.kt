package de.itshasan.iptv_client.overview.adapter.episodes

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_repository.network.IptvRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EpisodeViewHolder(
    private val view: View,
    private val onEpisodeClicked: ((Episode) -> Unit),
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val thumbImageView by lazy { view.findViewById<ImageView>(R.id.thumbImageView) }
    private val progressBar by lazy { view.findViewById<ProgressBar>(R.id.progressBar) }
    private val titleTextView by lazy { view.findViewById<TextView>(R.id.titleTextView) }
    private val plotTextView by lazy { view.findViewById<TextView>(R.id.plotTextView) }
    private val ratingTextView by lazy { view.findViewById<TextView>(R.id.ratingTextView) }
    private val releaseDateTextView by lazy { view.findViewById<TextView>(R.id.releaseDateTextView) }
    private val durationTextView by lazy { view.findViewById<TextView>(R.id.durationTextView) }

    fun onBind(episode: Episode, position: Int) {
        titleTextView.text = episode.title
        plotTextView.text = episode.info.plot ?: ""
        if (plotTextView.text == "")
            plotTextView.visibility = View.GONE
        else
            plotTextView.visibility = View.VISIBLE

        ratingTextView.text = episode.info.rating?.toString()
        if (ratingTextView.text == "0.0"
            || ratingTextView.text == "null"
            || ratingTextView.text == ""
        )
            ratingTextView.visibility = View.GONE
        else {
            ratingTextView.visibility = View.VISIBLE
            val result = String.format("%.1f", episode.info.rating)
            ratingTextView.text = result
        }
        releaseDateTextView.text = episode.info.releaseDate ?: ""
        if (releaseDateTextView.text == "")
            releaseDateTextView.visibility = View.INVISIBLE
        else {
            releaseDateTextView.visibility = View.VISIBLE
        }

        durationTextView.text = episode.info.duration

        Glide
            .with(view.context)
            .load(episode.info.movieImage)
            .centerCrop()
            .placeholder(R.color.gray)
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .into(thumbImageView)

        thumbImageView.setOnClickListener {
            onEpisodeClicked(episode)
        }

        GlobalScope.launch(Dispatchers.IO) {
            val watchHistory =
                iptvDatabase.watchHistoryDao().getSeriesItem(episode.id)
            if (watchHistory != null) {
                launch(Dispatchers.Main) {
                    progressBar?.visibility = View.VISIBLE
                    val progressValue = 100 * watchHistory.currentTime / watchHistory.totalTime
                    progressBar?.setProgress(progressValue.toInt(), true)
                }
            } else {
                launch(Dispatchers.Main) {
                    progressBar?.visibility = View.GONE
                }
            }
        }

    }

}