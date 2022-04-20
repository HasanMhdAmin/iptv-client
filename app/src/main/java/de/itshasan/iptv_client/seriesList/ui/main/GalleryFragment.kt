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
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.Constant.COVER_URL
import de.itshasan.iptv_core.model.Constant.SERIES_ID
import de.itshasan.iptv_core.model.Constant.SERIES_TITLE
import de.itshasan.iptv_core.model.series.SeriesItem


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
        val searchView = view.findViewById<SearchView>(R.id.search_view)
        searchView.setOnQueryTextListener(this)

        val categoryId = arguments?.getString(Constant.CATEGORY_ID)
        val viewModel: GalleryViewModel by viewModels {
            GalleryViewModelFactory(
                categoryId!!,
                requireActivity().application
            )
        }
        this.viewModel = viewModel
        categoriesRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3)
            adapter = viewModel.getAdapter().apply {

                onCategoryClicked =
                    { seriesItem: SeriesItem, imageView: ImageView, textview: TextView ->
                        Log.d(TAG, "onViewCreated: seriesId: ${seriesItem.seriesId}")

                        val extras = FragmentNavigatorExtras(
                            imageView to seriesItem.cover,
                            textview to seriesItem.name
                        )

                        val bundle = bundleOf(
                            SERIES_ID to seriesItem.seriesId,
                            COVER_URL to seriesItem.cover,
                            SERIES_TITLE to seriesItem.name
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