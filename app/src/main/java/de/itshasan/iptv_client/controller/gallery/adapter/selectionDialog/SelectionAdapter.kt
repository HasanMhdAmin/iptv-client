package de.itshasan.iptv_client.controller.gallery.adapter.selectionDialog

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_core.model.Selectable

class SelectionAdapter<S : Selectable>(
    var items: List<S>,
    var selectedItem: S?
) :
    RecyclerView.Adapter<SelectionViewHolder<S>>() {

    lateinit var onItemClicked: ((S) -> Unit)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SelectionViewHolder<S> {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_season, parent, false)
        return SelectionViewHolder(view, onItemClicked)
    }

    override fun onBindViewHolder(holder: SelectionViewHolder<S>, position: Int) {
        val item = items[position]
        val isSelected = selectedItem.let {
            selectedItem == item
        }
        holder.onBind(item, position, isSelected)
    }

    override fun getItemCount() = items.size
}