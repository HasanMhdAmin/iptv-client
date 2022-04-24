package de.itshasan.iptv_client.legacy.category

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.viewModelScope
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.legacy.category.adapter.CategoryAdapter
import de.itshasan.iptv_client.utils.navigator.Navigator
import de.itshasan.iptv_core.model.Constant
import de.itshasan.iptv_core.model.Constant.ALL_SERIES
import de.itshasan.iptv_core.model.category.SeriesCategories
import de.itshasan.iptv_core.model.category.Category
import de.itshasan.iptv_database.database.iptvDatabase
import de.itshasan.iptv_network.network.IptvNetwork
import de.itshasan.iptv_network.network.callback.SeriesCategoriesCallback
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


private val TAG = CategoryActivity::class.java.simpleName

class CategoryActivity : AppCompatActivity() {

    private val categoriesRecyclerView by lazy { findViewById<RecyclerView>(R.id.categoriesRecyclerView) }

    fun <K> increment(map: MutableMap<K, Int>, key: K) {
        map.putIfAbsent(key, 0)
        map[key] = map[key]!! + 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)


        ////
        GlobalScope.launch(Dispatchers.IO) {
            val categories = iptvDatabase.categoryDao().getAll()
            if (categories.isEmpty()) {
                loadSeriesCategories()
            } else {
                val seriesCategories = SeriesCategories()
                seriesCategories.addAll(categories)
                bindData(seriesCategories)
            }

        }
    }

    private fun bindData(seriesCategories: SeriesCategories) {
        val categoryAdapter = CategoryAdapter()

        categoryAdapter.setDataList(seriesCategories)

        categoriesRecyclerView.apply {
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = categoryAdapter.apply {
                onCategoryClicked = {
                    Navigator.goToSeriesList(this@CategoryActivity, it.categoryId)
                }
            }
        }
    }

    private fun loadSeriesCategories() {
        IptvNetwork.getSeriesCategories(object : SeriesCategoriesCallback() {
            override fun onSuccess(backendResponse: SeriesCategories) {
                Log.d(
                    TAG,
                    "onSuccess: getSeriesCategories seriesCategoriesCount: ${backendResponse.size}")
                // ALL_SERIES is id to get all the series.
                val allSeries =
                    Category(categoryId = ALL_SERIES,
                        categoryName = "All",
                        parentId = 0,
                        Constant.TYPE_SERIES)
                backendResponse.add(0, allSeries)
                GlobalScope.launch(Dispatchers.IO) {
                    backendResponse.forEach {
                        iptvDatabase.categoryDao().insert(it)
                    }
                }
                bindData(backendResponse)
            }

            override fun onError(status: Int, message: String) {
                Log.d(TAG, "onError: getSeriesCategories: message: $message ")
            }

        })

    }
}