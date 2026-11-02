package com.glebestraikh.leetcodereminder.data.repository

import com.glebestraikh.leetcodereminder.data.database.dao.ContentVersionDao
import com.glebestraikh.leetcodereminder.data.database.model.ContentVersion

interface ContentVersionStore {
    suspend fun getContentVersion(): Long

    suspend fun saveContentVersion(version: Long)
}

class LocalContentVersionStore(private val contentVersionDao: ContentVersionDao) : ContentVersionStore {
    override suspend fun getContentVersion(): Long {
        return contentVersionDao.getVersion() ?: 0L
    }

    override suspend fun saveContentVersion(version: Long) {
        contentVersionDao.upsert(ContentVersion(version = version))
    }
}
