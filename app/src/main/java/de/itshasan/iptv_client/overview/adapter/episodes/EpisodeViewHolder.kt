package de.itshasan.iptv_client.overview.adapter.episodes

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.info.Episode
import de.itshasan.iptv_database.database.iptvDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class EpisodeViewHolder(
    view: View,
    private val onEpisodeClicked: ((Episode) -> Unit),
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val thumbImageView by lazy { view.findViewById<ImageView>(R.id.thumbImageView) }
    private val progressBar by lazy { view.findViewById<ProgressBar>(R.id.progressBar) }
    private val titleTextView by lazy { view.findViewById<TextView>(R.id.titleTextView) }
    private val durationTextView by lazy { view.findViewById<TextView>(R.id.durationTextView) }

    fun onBind(episode: Episode, position: Int) {
        titleTextView.text = episode.title
        durationTextView.text = episode.info.duration
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