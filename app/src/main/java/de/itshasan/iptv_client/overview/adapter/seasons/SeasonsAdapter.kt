package de.itshasan.iptv_client.overview.adapter.seasons

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.info.season.Season

class SeasonsAdapter(
    var seasons: List<Season>,
    var selectedSeason: Season
) :
    RecyclerView.Adapter<SeasonsViewHolder>() {

    lateinit var onSeasonClicked: ((Season) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeasonsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_season, parent, false)
        return SeasonsViewHolder(view, onSeasonClicked)
    }

    override fun onBindViewHolder(holder: SeasonsViewHolder, position: Int) {
        val season = seasons[position]
        holder.onBind(season, position, selectedSeason == season)
    }

    override fun getItemCount() = seasons.size
}