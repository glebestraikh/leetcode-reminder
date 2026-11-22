package com.glebestraikh.leetcodereminder.util

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory

inline fun <reified VM : ViewModel> viewModelProviderFactoryOf(
    crossinline create: CreationExtras.() -> VM,
): ViewModelProvider.Factory = viewModelFactory {
    initializer {
        this.create()
    }
}
