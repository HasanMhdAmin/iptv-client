package de.itshasan.iptv_core.model.series.info.episode.episodInfo


import com.google.gson.annotations.SerializedName

data class Tags(
    @SerializedName("language")
    val language: String,
    @SerializedName("handler_name")
    val handlerName: String
)