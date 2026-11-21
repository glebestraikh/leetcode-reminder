package com.glebestraikh.leetcodereminder.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

/**
 * Returns a [ViewModelProvider.Factory] which will return the result of [create] when it's
 * [ViewModelProvider.Factory.create] function is called.
 *
 * Unlike in Jetcaster, [create] receives [CreationExtras], so it can call
 * `createSavedStateHandle()`.
 */
inline fun <reified VM : ViewModel> viewModelProviderFactoryOf(
    crossinline create: CreationExtras.() -> VM,
): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        this.create()
    }
}
