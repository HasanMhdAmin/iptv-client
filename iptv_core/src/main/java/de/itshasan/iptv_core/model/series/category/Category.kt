package de.itshasan.iptv_core.model.series.category


import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import de.itshasan.iptv_core.model.Selectable

@Entity
data class Category(
    @PrimaryKey
    @SerializedName("category_id")
    val categoryId: String,
    @SerializedName("category_name")
    val categoryName: String,
    @SerializedName("parent_id")
    val parentId: Int
) : Selectable {
    override fun getTitle(): String {
        return categoryName
    }
}