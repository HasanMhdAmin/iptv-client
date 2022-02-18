package de.itshasan.iptv_client.utils

import android.content.Context
import de.itshasan.iptv_client.parser.SimpleM3UParser
import java.io.InputStream

object Repo {

    fun getList(context: Context, source: Source): MutableList<SimpleM3UParser.M3U_Entry> {
        val inputStream: InputStream = context.assets.open("m3u/channels.m3u")
        val simpleM3UParser = SimpleM3UParser()
        val list = simpleM3UParser.parse(inputStream)

        val liveList = mutableListOf<SimpleM3UParser.M3U_Entry>()
        val seriesList = mutableListOf<SimpleM3UParser.M3U_Entry>()
        val moviesList = mutableListOf<SimpleM3UParser.M3U_Entry>()

        for (i in 0 until list.size) {
            when {
                list[i].url.contains("series") -> {
                    seriesList.add(list[i])
                }
                list[i].url.contains("movie") -> {
                    moviesList.add(list[i])
                }
                else -> {
                    liveList.add(list[i])
                }
            }
        }


        return when (source) {
            Source.SERIES -> {
                seriesList
            }
            Source.MOVIES -> {
                moviesList
            }
            else -> {
                liveList
            }
        }

    }
}