package de.itshasan.iptv_core.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(indices = [Index(value = ["uniqueId"], unique = true)])
data class Favourite(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val parentId: String,       // Series ID or Movie ID
    val name: String,
    val contentType: String,    // TODO: make it enum
    val timestamp: Long,
    val coverUrl: String,
    val uniqueId: String,
    val userId: String
)
