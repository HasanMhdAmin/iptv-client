package de.itshasan.iptv_client.overview.adapter.episodes

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.info.Episode

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
    }

}