package de.itshasan.iptv_client.seriesList.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.SeriesItem

class SeriesItemViewHolder(
    private val view: View,
    private val onItemClicked: ((SeriesItem, ImageView, TextView) -> Unit)? = null,
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val item by lazy { view.findViewById<CardView>(R.id.item) }
    private val itemName by lazy { view.findViewById<TextView>(R.id.itemName) }
    private val cover by lazy { view.findViewById<ImageView>(R.id.cover) }
    private val imdbRating by lazy { view.findViewById<TextView>(R.id.imdbRating) }

    fun onBind(seriesItem: SeriesItem, position: Int) {

        itemName.apply {
            transitionName = seriesItem.name
            text = seriesItem.name
        }

        item.setOnClickListener {
            onItemClicked?.let { it1 -> it1(seriesItem, cover, itemName) }
        }
        cover.apply {
            transitionName = seriesItem.cover
            Glide
                .with(view.context)
                .load(seriesItem.cover)
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)

        }


        imdbRating.apply {
            visibility =
                if (seriesItem.rating.isBlank()
                    || seriesItem.rating == ""
                    || seriesItem.rating == "0"
                ) View.GONE else View.VISIBLE
            text = seriesItem.rating
        }

    }


}
