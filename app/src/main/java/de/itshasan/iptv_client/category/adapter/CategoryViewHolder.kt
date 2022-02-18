package de.itshasan.iptv_client.category.adapter

import android.view.View
import android.widget.TextView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.model.CategoryCount

class CategoryViewHolder(
    view: View,
    private val onCategoryClicked: ((String) -> Unit)? = null,
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val categoryName by lazy { view.findViewById<TextView>(R.id.categoryName) }

    fun onBind(category: CategoryCount, position: Int) {

        categoryName.apply {
            text = category.categoryName + "\n" + category.count
        }

    }


}
