package com.glebestraikh.leetcodereminder.data.database.dao

import androidx.room.Dao
import androidx.room.Query
import com.glebestraikh.leetcodereminder.data.database.model.ContentVersion

@Dao
abstract class ContentVersionDao : BaseDao<ContentVersion> {
    @Query("SELECT version FROM content_version LIMIT 1")
    abstract suspend fun getVersion(): Long?
}
