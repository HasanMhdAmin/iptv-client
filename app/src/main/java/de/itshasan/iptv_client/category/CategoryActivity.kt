package de.itshasan.iptv_client.category

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import de.itshasan.iptv_client.R
import de.itshasan.iptv_client.category.adapter.CategoryAdapter
import de.itshasan.iptv_client.model.CategoryCount
import de.itshasan.iptv_client.utils.Repo
import de.itshasan.iptv_client.utils.Source

class CategoryActivity : AppCompatActivity() {

    private val categoriesRecyclerView by lazy { findViewById<RecyclerView>(R.id.categoriesRecyclerView) }

    fun <K> increment(map: MutableMap<K, Int>, key: K) {
        map.putIfAbsent(key, 0)
        map[key] = map[key]!! + 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category)

        val listString = mutableListOf<String>();
        listString.add("test01");
        listString.add("test02");
        listString.add("test03");
        listString.add("test04");
        listString.add("test05");
        listString.add("test06");


        val seriesList = Repo.getList(this, Source.SERIES)

        val map: MutableMap<String, Int> = HashMap()

        val categoryCountList = mutableListOf<CategoryCount>()

        for (item in seriesList) {
            increment(map, item.groupTitle)
        }

        val sortedMap = map.toSortedMap()
        sortedMap.map {
            categoryCountList.add(CategoryCount(it.key, it.value))
        }

        var valuesList = ArrayList(map.keys).toMutableList()


        val categoryAdapter = CategoryAdapter(categoryCountList)

        categoriesRecyclerView.apply {
//            layoutManager = GridLayoutManager(this@CategoryActivity, 2)
            layoutManager = androidx.recyclerview.widget.LinearLayoutManager(context)
            adapter = categoryAdapter.apply {
                onCategoryClicked = {

                }
            }
        }
    }
}