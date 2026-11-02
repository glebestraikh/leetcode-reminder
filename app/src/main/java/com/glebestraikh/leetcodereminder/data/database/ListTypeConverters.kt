package com.glebestraikh.leetcodereminder.data.database

import androidx.room.TypeConverter
import com.glebestraikh.leetcodereminder.data.database.model.CodeLine
import com.glebestraikh.leetcodereminder.data.database.model.Example
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

object ListTypeConverters {
    @TypeConverter
    fun toStringList(value: String): List<String> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromStringList(list: List<String>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toExampleList(value: String): List<Example> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromExampleList(list: List<Example>): String {
        return Json.encodeToString(list)
    }

    @TypeConverter
    fun toCodeLineList(value: String): List<CodeLine> {
        return Json.decodeFromString(value)
    }

    @TypeConverter
    fun fromCodeLineList(list: List<CodeLine>): String {
        return Json.encodeToString(list)
    }
}
