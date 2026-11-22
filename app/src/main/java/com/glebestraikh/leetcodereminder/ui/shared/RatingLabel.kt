package com.glebestraikh.leetcodereminder.ui.shared

import androidx.annotation.StringRes
import com.glebestraikh.leetcodereminder.R
import com.glebestraikh.leetcodereminder.model.Rating

@get:StringRes
val Rating.labelRes: Int
    get() = when (this) {
        Rating.FORGOT -> R.string.rating_forgot
        Rating.NEED_TO_REPEAT -> R.string.rating_need_to_repeat
        Rating.REMEMBER_WELL -> R.string.rating_remember_well
    }
