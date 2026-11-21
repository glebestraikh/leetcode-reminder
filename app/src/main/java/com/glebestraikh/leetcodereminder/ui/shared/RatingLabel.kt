package com.glebestraikh.leetcodereminder.ui.shared

import androidx.annotation.StringRes
import com.glebestraikh.leetcodereminder.R
import com.glebestraikh.leetcodereminder.data.database.model.Rating

/** The text of a rating, shown on the rating buttons and next to the check mark of a solved problem. */
@get:StringRes
val Rating.labelRes: Int
    get() = when (this) {
        Rating.FORGOT -> R.string.rating_forgot
        Rating.NEED_TO_REPEAT -> R.string.rating_need_to_repeat
        Rating.REMEMBER_WELL -> R.string.rating_remember_well
    }
