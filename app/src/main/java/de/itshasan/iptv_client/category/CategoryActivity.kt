package de.itshasan.iptv_client.category

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.category.adapter.CategoryAdapter
import de.itshasan.iptv_repository.network.IptvRepository
import de.itshasan.iptv_repository.network.callback.SeriesCategoriesCallback
import de.itshasan.iptv_core.model.SeriesCategories


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

        IptvRepository.getSeriesCategories(object : SeriesCategoriesCallback() {
            override fun onSuccess(backendResponse: SeriesCategories) {
                Log.d(TAG, "onSuccess: seriesCategoriesCount: ${backendResponse.size}")
                val categoryAdapter = CategoryAdapter(backendResponse)

                categoriesRecyclerView.apply {
                    layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
                    adapter = categoryAdapter.apply {
                        onCategoryClicked = {

                        }
                    }
                }
            }

            override fun onError(status: Int, message: String) {
                Log.d(TAG, "onError: ")
            }

        })

    }
}