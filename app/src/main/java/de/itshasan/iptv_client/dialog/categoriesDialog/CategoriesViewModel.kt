package de.itshasan.iptv_client.dialog.categoriesDialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.category.Category
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_network.network.IptvNetwork
import de.itshasan.iptv_network.network.callback.CategoriesCallback
import de.itshasan.iptv_network.network.callback.SeriesCategoriesCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    var categories: MutableLiveData<ArrayList<Category>> =
        MutableLiveData<ArrayList<Category>>()

    private fun loadSeriesCategories() {
        IptvNetwork.getSeriesCategories(object : SeriesCategoriesCallback() {
            override fun onSuccess(backendResponse: ArrayList<Category>) {

                // ALL_SERIES is id to get all the series.
                val allSeries =
                    Category(
                        categoryId = Constant.ALL_SERIES,
                        categoryName = "All",
                        parentId = 0,
                        Constant.TYPE_SERIES
                    )
                backendResponse.add(0, allSeries)
                viewModelScope.launch(Dispatchers.IO) {
                    backendResponse.forEach {
                        it.type = Constant.TYPE_SERIES
                        iptvDatabase.categoryDao().insert(it)
                    }
                }
                categories.postValue(backendResponse)
            }

            override fun onError(status: Int, message: String) {
            }
        })
    }

    private fun getSeriesCategories() {
        viewModelScope.launch(Dispatchers.IO) {
            val categories = iptvDatabase.categoryDao().getAllSeriesCategory()
            if (categories.isEmpty()) {
                loadSeriesCategories()
            } else {
                this@CategoriesViewModel.categories.postValue(categories as ArrayList<Category>)
            }
        }
    }

    private fun loadMoviesCategories() {
        IptvNetwork.getMoviesCategories(object : CategoriesCallback() {
            override fun onSuccess(backendResponse: ArrayList<Category>) {
                // ALL_MOVIES is id to get all movies.
                val allSeries =
                    Category(
                        categoryId = Constant.ALL_MOVIES,
                        categoryName = "All",
                        parentId = 0,
                        Constant.TYPE_SERIES
                    )
                backendResponse.add(0, allSeries)
                viewModelScope.launch(Dispatchers.IO) {
                    backendResponse.forEach {
                        it.type = Constant.TYPE_MOVIES
                        iptvDatabase.categoryDao().insert(it)
                    }
                }
                categories.postValue(backendResponse)
            }

            override fun onError(status: Int, message: String) {

            }

        })
    }

    private fun getMoviesCategory() {
        viewModelScope.launch(Dispatchers.IO) {
            val categories = iptvDatabase.categoryDao().getAllMoviesCategory()
            if (categories.isEmpty()) {
                loadMoviesCategories()
            } else {
                this@CategoriesViewModel.categories.postValue(categories as ArrayList<Category>)
            }
        }
    }

    fun getCategories(target: String) {
        if (target == Constant.TYPE_SERIES) {
            getSeriesCategories()
        } else if (target == Constant.TYPE_MOVIES) {

            getMoviesCategory()
        }
    }
}