package de.itshasan.iptv_client.controller.contentInfo.adapter.episodes

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.info.Episode

class EpisodeAdapter(
    var episodes: List<Episode>,
    var currentEpisode: Episode? = null,
) :
    RecyclerView.Adapter<EpisodeViewHolder>() {

    lateinit var onEpisodeClicked: ((Episode) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): EpisodeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_episode, parent, false)
        return EpisodeViewHolder(view, onEpisodeClicked)
    }

    override fun onBindViewHolder(holder: EpisodeViewHolder, position: Int) {
        val episode = episodes[position]
        holder.onBind(episode, position, currentEpisode)
    }

    override fun getItemCount() = episodes.size


}