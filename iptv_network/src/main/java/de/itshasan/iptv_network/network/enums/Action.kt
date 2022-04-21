package de.itshasan.iptv_network.network.enums

enum class Action(val value: String) {
    GET_SERIES_CATEGORIES("get_series_categories"),
    GET_SERIES("get_series"),
    GET_SERIES_INFO("get_series_info"),

    GET_MOVIES("get_vod_streams")
}