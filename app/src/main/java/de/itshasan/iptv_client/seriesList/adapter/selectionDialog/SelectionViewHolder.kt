package de.itshasan.iptv_client.seriesList.adapter.selectionDialog

import android.util.TypedValue
import android.view.View
import android.widget.TextView
import androidx.core.widget.TextViewCompat
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.Selectable

class SelectionViewHolder<S : Selectable>(
    view: View,
    private val onItemClicked: ((S) -> Unit),
) : androidx.recyclerview.widget.RecyclerView.ViewHolder(view) {

    private val itemName by lazy { view.findViewById<TextView>(R.id.itemName) }

    fun onBind(item: S, position: Int, isSelected: Boolean) {

        itemName.apply {
            text = item.getTitle()
            if (isSelected) {
                TextViewCompat.setTextAppearance(this, R.style.TextViewBoldColor)
                setTextSize(TypedValue.COMPLEX_UNIT_PX, resources.getDimension(R.dimen.selected_season_textview_size))
            }
            setOnClickListener {
                onItemClicked(item)
            }
        }

    }
}