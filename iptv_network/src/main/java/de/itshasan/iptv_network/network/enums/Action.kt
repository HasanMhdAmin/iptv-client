package de.itshasan.iptv_network.network.enums

enum class Action(val value: String) {
    GET_SERIES_CATEGORIES("get_series_categories"),
    GET_SERIES("get_series"),
    GET_SERIES_INFO("get_series_info"),

    GET_MOVIES("get_vod_streams"),
    GET_MOVIES_INFO("get_vod_info"),
    GET_MOVIES_CATEGORIES("get_vod_categories")
}