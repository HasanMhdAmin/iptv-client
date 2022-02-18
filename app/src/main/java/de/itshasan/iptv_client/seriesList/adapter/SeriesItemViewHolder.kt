package de.itshasan.iptv_client.seriesList.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.SeriesItem

class SeriesItemViewHolder(
    private val view: View,
    private val onItemClicked: ((SeriesItem) -> Unit)? = null,
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val item by lazy { view.findViewById<CardView>(R.id.item) }
    private val itemName by lazy { view.findViewById<TextView>(R.id.itemName) }
    private val cover by lazy { view.findViewById<ImageView>(R.id.cover) }

    fun onBind(seriesItem: SeriesItem, position: Int) {

        itemName.apply {
            text = seriesItem.name
        }

        item.setOnClickListener {
            onItemClicked?.let { it1 -> it1(seriesItem) }
        }

        Glide
            .with(view.context)
            .load(seriesItem.cover)
            .centerCrop()
            .into(cover)

    }


}
