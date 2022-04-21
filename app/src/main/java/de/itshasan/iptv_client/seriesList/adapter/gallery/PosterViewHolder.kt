package de.itshasan.iptv_client.seriesList.adapter.gallery

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.Posterable

class PosterViewHolder<P : Posterable>(
    private val view: View,
    private val onItemClicked: ((P, ImageView, TextView) -> Unit)? = null,
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val item by lazy { view.findViewById<CardView>(R.id.item) }
    private val itemName by lazy { view.findViewById<TextView>(R.id.itemName) }
    private val cover by lazy { view.findViewById<ImageView>(R.id.cover) }
    private val imdbRating by lazy { view.findViewById<TextView>(R.id.imdbRating) }

    fun onBind(posterable: P, position: Int) {

        itemName.apply {
            transitionName = posterable.getTitle()
            text = posterable.getTitle()
        }

        item.setOnClickListener {
            onItemClicked?.let { it1 -> it1(posterable, cover, itemName) }
        }
        cover.apply {
            transitionName = posterable.getPosterUrl()
            Glide
                .with(view.context)
                .load(posterable.getPosterUrl())
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(this)

        }


        imdbRating.apply {
            visibility =
                if (posterable.getImdbRating().isBlank()
                    || posterable.getImdbRating() == ""
                    || posterable.getImdbRating() == "0"
                ) View.GONE else View.VISIBLE
            text = posterable.getImdbRating()
        }

    }


}
