package de.itshasan.iptv_client.legacy.category.adapter

import android.view.View
import android.widget.TextView
import androidx.cardview.widget.CardView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.series.category.SeriesCategoriesItem

class CategoryViewHolder(
    view: View,
    private val onCategoryClicked: ((SeriesCategoriesItem) -> Unit)? = null,
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val item by lazy { view.findViewById<CardView>(R.id.item) }
    private val categoryName by lazy { view.findViewById<TextView>(R.id.categoryName) }

    fun onBind(category: SeriesCategoriesItem, position: Int) {

        categoryName.apply {
            text = category.categoryName
        }

        item.setOnClickListener {
            onCategoryClicked?.let { it1 -> it1(category) }
        }

    }


}
