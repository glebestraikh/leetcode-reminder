package com.glebestraikh.leetcodereminder.data.database.dao

import androidx.room.Upsert

interface BaseDao<T> {
    @Upsert
    suspend fun upsert(entity: T)

    @Upsert
    suspend fun upsertAll(entities: Collection<T>)
}
