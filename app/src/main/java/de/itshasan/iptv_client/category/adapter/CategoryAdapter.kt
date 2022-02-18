package de.itshasan.iptv_client.category.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.category.SeriesCategoriesItem

class CategoryAdapter : RecyclerView.Adapter<CategoryViewHolder>() {

    private var categories: MutableList<SeriesCategoriesItem> = mutableListOf()

    var onCategoryClicked: ((SeriesCategoriesItem) -> Unit)? = null

    fun setDataList(data: ArrayList<SeriesCategoriesItem>) {
        this.categories = data
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_category, parent, false)
        return CategoryViewHolder(view, onCategoryClicked)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val cat = categories[position]
        holder.onBind(cat, position)
    }

    override fun getItemCount(): Int = categories.size

}