package com.glebestraikh.leetcodereminder

import android.app.Application
import kotlinx.coroutines.MainScope

/**
 * Application which sets up our dependency [Graph] with a context
 * and starts loading the bundled catalog.
 */
class LeetCodeReminderApplication : Application() {
    // Lives as long as the app process. MainScope uses a SupervisorJob,
    // so a failure in one coroutine does not cancel the others.
    private val applicationScope = MainScope()

    override fun onCreate() {
        super.onCreate()
        Graph.provide(this, applicationScope)
    }
}
