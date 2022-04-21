package de.itshasan.iptv_core.model.movie

data class MoviePoster(
    val added: String,
    val category_id: String,
    val container_extension: String,
    val custom_sid: String,
    val direct_source: String,
    val is_adult: String,
    val name: String,
    val num: Int,
    val rating: String,
    val rating_5based: Int,
    val stream_icon: String,
    val stream_id: Int,
    val stream_type: String
)