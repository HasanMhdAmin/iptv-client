package de.itshasan.iptv_client.controller.gallery.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.dialog.categoriesDialog.CategoriesDialog
import de.itshasan.iptv_client.databinding.GalleryFragmentBinding
import de.itshasan.iptv_core.CoreFragment
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.Constant.ALL_SERIES
import de.itshasan.iptv_core.model.Constant.CONTENT_ID
import de.itshasan.iptv_core.model.Constant.COVER_URL
import de.itshasan.iptv_core.model.Constant.SERIES_TITLE
import de.itshasan.iptv_core.model.Constant.TARGET
import de.itshasan.iptv_core.model.Constant.TYPE_MOVIES
import de.itshasan.iptv_core.model.Posterable
import de.itshasan.iptv_core.model.category.Category


private val TAG = GalleryFragment::class.java.simpleName

class GalleryFragment : CoreFragment<GalleryFragmentBinding, GalleryViewModel>(),
    SearchView.OnQueryTextListener {

    companion object {
        fun newInstance() = GalleryFragment()
    }

    override fun provideBinding(
        layoutInflater: LayoutInflater,
        container: ViewGroup?
    ) = GalleryFragmentBinding.inflate(layoutInflater, container, false)

    override fun provideViewModel() =
        ViewModelProvider(this)[GalleryViewModel::class.java]

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.searchLayout.searchView.setOnQueryTextListener(this)

        val target = arguments?.getString(Constant.TARGET, TYPE_MOVIES)!!
        val categoryId = arguments?.getString(Constant.CATEGORY_ID, ALL_SERIES)!!

        viewModel.setCategory(target, categoryId)

        binding.categoriesRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3)
            adapter = viewModel.getAdapter().apply {

                onItemClicked =
                    { posterable: Posterable, imageView: ImageView, textview: TextView ->

                        val extras = FragmentNavigatorExtras(
                            imageView to posterable.getPosterUrl(),
                            textview to posterable.getTitle()
                        )

                        val bundle = bundleOf(
                            TARGET to target,
                            CONTENT_ID to posterable.getId(),
                            COVER_URL to posterable.getPosterUrl(),
                            SERIES_TITLE to posterable.getTitle()
                        )

                        findNavController().navigate(
                            R.id.action_navigation_gallery_to_navigation_details,
                            bundle,
                            null,
                            extras
                        )
                    }
            }
        }

        binding.categoryTextView.setOnClickListener {
            val categoriesDialog = CategoriesDialog.newInstance<Category>(target, null) {
                Log.d(TAG, it.getTitle())
                viewModel.setCategory(target, it.categoryId)
                binding.categoryTextView.text = it.categoryName
            }
            categoriesDialog.show(parentFragmentManager, TAG)
        }

        viewModel.getRecyclerListDataObserver()
            .observe(requireActivity()) {
                if (it != null) {
                    //update the adapter
                    viewModel.setAdapterData(it)

                } else {

                    Log.d(TAG, "onViewCreated: ")
                }
            }

    }

    override fun onQueryTextSubmit(query: String?): Boolean {
        viewModel.getAdapter().filter.filter(query)
        return false
    }

    override fun onQueryTextChange(query: String?): Boolean {
        viewModel.getAdapter().filter.filter(query)
        return false
    }


}