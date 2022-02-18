package de.itshasan.iptv_client.seriesList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.SeriesItem

class GalleryAdapter : RecyclerView.Adapter<SeriesItemViewHolder>() {

    private var categories: MutableList<SeriesItem> = mutableListOf()

    var onCategoryClicked: ((SeriesItem) -> Unit)? = null

    fun setDataList(data: ArrayList<SeriesItem>) {
        this.categories = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_series, parent, false)
        return SeriesItemViewHolder(view, onCategoryClicked)
    }

    override fun onBindViewHolder(holder: SeriesItemViewHolder, position: Int) {
        val cat = categories[position]
        holder.onBind(cat, position)
    }

    override fun getItemCount(): Int = categories.size

}