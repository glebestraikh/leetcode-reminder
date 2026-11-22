package com.glebestraikh.leetcodereminder

import android.app.Application
import kotlinx.coroutines.MainScope

class LeetCodeReminderApplication : Application() {
    private val applicationScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this, applicationScope)
    }
}
