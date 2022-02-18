package de.itshasan.iptv_core.model


import com.google.gson.annotations.SerializedName

data class SeriesCategoriesItem(
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("parent_id")
    val parentId: Int
)