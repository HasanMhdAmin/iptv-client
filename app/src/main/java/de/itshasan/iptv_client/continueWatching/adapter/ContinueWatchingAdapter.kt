package de.itshasan.iptv_client.continueWatching.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.WatchHistory

class ContinueWatchingAdapter(
    var watchHistoryList: List<WatchHistory>,
) :
    RecyclerView.Adapter<ContinueWatchingViewHolder>() {

    lateinit var onWatchHistoryClicked: ((WatchHistory) -> Unit)
    lateinit var onWatchHistoryLongClicked: ((WatchHistory) -> Unit)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContinueWatchingViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_continue_watching, parent, false)
        return ContinueWatchingViewHolder(view, onWatchHistoryClicked, onWatchHistoryLongClicked)
    }

    override fun onBindViewHolder(holder: ContinueWatchingViewHolder, position: Int) {
        val watchHistory = watchHistoryList[position]
        holder.onBind(watchHistory, position)
    }

    override fun getItemCount() = watchHistoryList.size
}