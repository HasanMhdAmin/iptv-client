package de.itshasan.iptv_client.seriesList.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.SeriesItem

class GalleryAdapter : RecyclerView.Adapter<SeriesItemViewHolder>(), Filterable {

    private var categories: MutableList<SeriesItem> = mutableListOf()
    private var categoriesFiltered: MutableList<SeriesItem> = mutableListOf()

    var onCategoryClicked: ((SeriesItem) -> Unit)? = null

    fun setDataList(data: ArrayList<SeriesItem>) {
        this.categories = data
        this.categoriesFiltered = this.categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SeriesItemViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_series, parent, false)
        return SeriesItemViewHolder(view, onCategoryClicked)
    }

    override fun onBindViewHolder(holder: SeriesItemViewHolder, position: Int) {
        val cat = categoriesFiltered[position]
        holder.onBind(cat, position)
    }

    override fun getItemCount(): Int = categoriesFiltered.size

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) categoriesFiltered = categories else {
                    val filteredList = ArrayList<SeriesItem>()
                    categories
                        .filter {
                            (it.name.contains(constraint!!, ignoreCase = true)) or
                                (it.genre.contains(constraint, ignoreCase = true))

                        }
                        .forEach { filteredList.add(it) }
                    categoriesFiltered = filteredList

                }
                return FilterResults().apply { values = categoriesFiltered }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {

                categoriesFiltered = if (results?.values == null)
                    ArrayList()
                else
                    results.values as ArrayList<SeriesItem>
                notifyDataSetChanged()
            }
        }
    }

}