package de.itshasan.iptv_client.seriesList.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.SearchView
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.seriesList.dialog.CategoriesDialog
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.Constant.ALL_SERIES
import de.itshasan.iptv_core.model.Constant.COVER_URL
import de.itshasan.iptv_core.model.Constant.CONTENT_ID
import de.itshasan.iptv_core.model.Constant.SERIES_TITLE
import de.itshasan.iptv_core.model.Constant.TARGET
import de.itshasan.iptv_core.model.Constant.TYPE_MOVIES
import de.itshasan.iptv_core.model.Posterable
import de.itshasan.iptv_core.model.series.category.SeriesCategoriesItem


private val TAG = GalleryFragment::class.java.simpleName

class GalleryFragment : Fragment(), SearchView.OnQueryTextListener {

    companion object {
        fun newInstance() = GalleryFragment()
    }

    lateinit var viewModel: GalleryViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        return inflater.inflate(R.layout.gallery_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesRecyclerView = view.findViewById<RecyclerView>(R.id.categoriesRecyclerView)
        val categoryTextView = view.findViewById<TextView>(R.id.categoryTextView)
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(this)

        val target = arguments?.getString(Constant.TARGET, TYPE_MOVIES)!!
        val categoryId = arguments?.getString(Constant.CATEGORY_ID, ALL_SERIES)!!
        val viewModel: GalleryViewModel by viewModels {
            GalleryViewModelFactory(
                categoryId,
                requireActivity().application
            )
        }

        this.viewModel = viewModel
        viewModel.setCategory(target, categoryId)

        categoriesRecyclerView.apply {
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

        categoryTextView.setOnClickListener {
            val categoriesDialog = CategoriesDialog.newInstance<SeriesCategoriesItem>(null) {
                Log.d(TAG, it.getTitle())
                viewModel.setCategory(target, it.categoryId)
                categoryTextView.text = it.categoryName
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