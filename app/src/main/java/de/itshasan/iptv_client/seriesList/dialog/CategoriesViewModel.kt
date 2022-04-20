package de.itshasan.iptv_client.seriesList.dialog

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.series.category.SeriesCategories
import de.itshasan.iptv_core.model.series.category.SeriesCategoriesItem
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.SeriesCategoriesCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class CategoriesViewModel : ViewModel() {

    var seriesCategories: MutableLiveData<SeriesCategories> = MutableLiveData<SeriesCategories>()

    private fun loadSeriesCategories() {
        IptvRepository.getSeriesCategories(object : SeriesCategoriesCallback() {
            override fun onSuccess(backendResponse: SeriesCategories) {

                // ALL_SERIES is id to get all the series.
                val allSeries =
                    SeriesCategoriesItem(
                        categoryId = Constant.ALL_SERIES,
                        categoryName = "All",
                        parentId = 0
                    )
                backendResponse.add(0, allSeries)
                GlobalScope.launch(Dispatchers.IO) {
                    backendResponse.forEach {
                        iptvDatabase.seriesCategoryDao().insert(it)
                    }
                }
                seriesCategories.postValue(backendResponse)
            }

            override fun onError(status: Int, message: String) {

            }

        })

    }

    fun getSeriesCategories() {

        viewModelScope.launch(Dispatchers.IO) {
            val categories = iptvDatabase.seriesCategoryDao().getAll()
            if (categories.isEmpty()) {
                loadSeriesCategories()
            } else {
                val seriesCategoriesContainer = SeriesCategories()
                seriesCategoriesContainer.addAll(categories)
                seriesCategories.postValue(seriesCategoriesContainer)

            }

        }
    }
}