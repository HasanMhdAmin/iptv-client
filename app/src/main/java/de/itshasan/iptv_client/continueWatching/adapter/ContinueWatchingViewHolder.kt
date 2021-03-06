package de.itshasan.iptv_client.continueWatching.adapter

import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import com.bumptech.glide.Glide
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.WatchHistory


class ContinueWatchingViewHolder(
    val view: View,
    private val onItemClicked: ((WatchHistory) -> Unit),
    private val onItemLongClicked: ((WatchHistory) -> Unit),
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val progress by lazy { view.findViewById<View>(R.id.progress) }
    private val title by lazy { view.findViewById<TextView>(R.id.title) }
    private val item by lazy { view.findViewById<CardView>(R.id.item) }
    private val coverImageView by lazy { view.findViewById<ImageView>(R.id.coverImageView) }

    fun onBind(watchHistory: WatchHistory, position: Int) {
        val progressBar = progress.findViewById<ProgressBar>(R.id.progress)
        val progressValue = 100 * watchHistory.currentTime / watchHistory.totalTime

        progressBar.progress = progressValue.toInt()
        title.text = watchHistory.name


        Glide.with(view.context)
            .load(watchHistory.coverUrl)
            .centerCrop()
            .into(coverImageView)

        item.setOnClickListener {
            onItemClicked(watchHistory)
        }

        item.setOnLongClickListener {
            onItemLongClicked(watchHistory)
            true
        }
    }


}