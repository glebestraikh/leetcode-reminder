package com.glebestraikh.leetcodereminder.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.glebestraikh.leetcodereminder.data.database.model.ContentInfo

@Dao
abstract class ContentInfoDao {
    @Query("SELECT * FROM content_info WHERE id = 0")
    abstract suspend fun getContentInfo(): ContentInfo?

    @Upsert
    abstract suspend fun upsert(contentInfo: ContentInfo)
}
