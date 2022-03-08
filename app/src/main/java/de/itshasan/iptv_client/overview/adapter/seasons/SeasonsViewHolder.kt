package de.itshasan.iptv_client.overview.adapter.seasons

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.info.season.Season

class SeasonsViewHolder(
    view: View,
    private val onSeasonClicked: ((Season) -> Unit),
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val seasonName by lazy { view.findViewById<TextView>(R.id.seasonName) }

    fun onBind(season: Season, position: Int, isSelected: Boolean) {

        seasonName.apply {
            text = season.name
            if (isSelected) {
                TextViewCompat.setTextAppearance(this, R.style.TextViewBoldColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.selected_season_textview_size))
            }
            setOnClickListener {
                onSeasonClicked(season)
            }
        }

    }
}