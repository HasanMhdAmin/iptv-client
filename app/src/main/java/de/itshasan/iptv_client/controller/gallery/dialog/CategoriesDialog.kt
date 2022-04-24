package de.itshasan.iptv_client.controller.gallery.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import androidx.lifecycle.ViewModelProvider
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.databinding.DialogCategoriesBinding
import de.itshasan.iptv_client.controller.gallery.adapter.selectionDialog.SelectionAdapter
import de.itshasan.iptv_core.CoreDialog
import de.itshasan.iptv_core.model.Selectable
import de.itshasan.iptv_core.model.series.category.SeriesCategoriesItem

class CategoriesDialog<S: Selectable>(
    private val selectedItem: S?,
    var onItemSelected: ((S) -> Unit)
) :
    CoreDialog<DialogCategoriesBinding>(R.layout.dialog_categories) {

    companion object {
        fun <S: Selectable> newInstance(selectedItem: S?, onItemSelected: ((S) -> Unit)) =
            CategoriesDialog(selectedItem, onItemSelected)
    }


    override fun provideBinding(layoutInflater: LayoutInflater) =
        DialogCategoriesBinding.inflate(layoutInflater)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupView(view)
    }

    private fun setupView(view: View) {
        val viewModel = ViewModelProvider(this)[CategoriesViewModel::class.java]

        viewModel.getSeriesCategories()

        viewModel.seriesCategories.observe(requireActivity()) {
            val selectionAdapter =
                SelectionAdapter(
                    items = it,
                    selectedItem = selectedItem as SeriesCategoriesItem?
                )

            binding.categoriesRecyclerView.apply {
                layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                adapter = selectionAdapter.apply {
                    onItemClicked = { selectable ->
                        onItemSelected(selectable as S)
                        dismiss()
                    }
                }
            }

            binding.closeActionButton.setOnClickListener {
                dismiss()
            }
        }
    }

}