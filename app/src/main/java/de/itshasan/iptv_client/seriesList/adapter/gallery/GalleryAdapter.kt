package de.itshasan.iptv_client.seriesList.adapter.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.Posterable

class GalleryAdapter<P : Posterable> : RecyclerView.Adapter<PosterViewHolder<P>>(), Filterable {

    private var categories: MutableList<P> = mutableListOf()
    private var categoriesFiltered: MutableList<P> = mutableListOf()

    var onItemClicked: ((P, ImageView, TextView) -> Unit)? = null

    fun setDataList(data: ArrayList<P>) {
        this.categories = data
        this.categoriesFiltered = this.categories
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PosterViewHolder<P> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_series, parent, false)
        return PosterViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: PosterViewHolder<P>, position: Int) {
        val cat = categoriesFiltered[position]
        holder.onBind(cat, position)
    }

    override fun getItemCount(): Int = categoriesFiltered.size

    override fun getFilter(): Filter {

        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint?.toString() ?: ""
                if (charString.isEmpty()) categoriesFiltered = categories else {
                    val filteredList = ArrayList<P>()
                    categories
                        .filter {
                            (it.getTitle().contains(constraint!!, ignoreCase = true))
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
                    results.values as ArrayList<P>
                notifyDataSetChanged()
            }
        }
    }

}