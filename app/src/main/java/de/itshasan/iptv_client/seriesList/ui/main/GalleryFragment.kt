package de.itshasan.iptv_client.seriesList.ui.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.EXTRA_MESSAGE
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.seriesList.GalleryActivity
import de.itshasan.iptv_core.model.Constant

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

        val categoryId = activity?.intent?.extras?.getString(Constant.CATEGORY_ID)
        val viewModel: GalleryViewModel by viewModels { GalleryViewModelFactory(categoryId!!) }
        this.viewModel = viewModel
        categoriesRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.GridLayoutManager(context, 3)
            adapter = viewModel.getAdapter().apply {
                onCategoryClicked = {
                    val intent =
                        Intent(context, GalleryActivity::class.java).apply {
                            putExtra(EXTRA_MESSAGE, it.categoryId)
                        }
                    startActivity(intent)
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