package com.glebestraikh.leetcodereminder.data.database.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "content_info")
data class ContentInfo(
    @PrimaryKey @ColumnInfo(name = "id") val id: Int = 0,
    @ColumnInfo(name = "problems_hash") val problemsHash: String,
)
