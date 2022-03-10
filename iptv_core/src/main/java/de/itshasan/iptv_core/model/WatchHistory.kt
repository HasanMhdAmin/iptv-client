package de.itshasan.iptv_core.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["contentId"], unique = true)])
data class WatchHistory(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val contentId: String,
    val parentId: String,       // Series ID
    val name: String,
    val contentType: String,    // TODO: make it enum
    val timestamp: Long,
    val currentTime: Long,
    val totalTime: Long,
)
