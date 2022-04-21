package de.itshasan.iptv_core.model

interface Posterable {
    fun getId(): Int
    fun getTitle(): String
    fun getPosterUrl(): String
    fun getImdbRating(): String
}