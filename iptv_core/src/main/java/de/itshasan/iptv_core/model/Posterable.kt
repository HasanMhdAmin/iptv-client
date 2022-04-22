package de.itshasan.iptv_core.model

public interface Posterable : Streamable {
    fun getId(): Int
    fun getTitle(): String
    fun getPosterUrl(): String
    fun getImdbRating(): String
}