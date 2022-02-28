package de.itshasan.iptv_client.overview.adapter.seasons

import android.view.View
import android.widget.TextView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.info.season.Season

class SeasonsViewHolder(
    view: View,
    private val onSeasonClicked: ((Season) -> Unit)? = null,
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val seasonName by lazy { view.findViewById<TextView>(R.id.seasonName) }

    fun onBind(season: Season, position: Int) {

        seasonName.apply {
            text = season.name
        }

    }
}