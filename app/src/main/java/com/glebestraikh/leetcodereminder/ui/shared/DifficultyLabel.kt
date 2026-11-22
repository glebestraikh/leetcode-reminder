package com.glebestraikh.leetcodereminder.ui.shared

import androidx.annotation.StringRes
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.glebestraikh.leetcodereminder.R
import com.glebestraikh.leetcodereminder.model.Difficulty

@Composable
fun DifficultyLabel(
    difficulty: Difficulty,
    modifier: Modifier = Modifier,
) {
    Text(
        text = stringResource(difficulty.labelRes),
        modifier = modifier,
        color = difficulty.color,
        style = MaterialTheme.typography.labelLarge,
    )
}

@get:StringRes
val Difficulty.labelRes: Int
    get() = when (this) {
        Difficulty.EASY -> R.string.difficulty_easy
        Difficulty.MEDIUM -> R.string.difficulty_medium
        Difficulty.HARD -> R.string.difficulty_hard
    }

private val Difficulty.color: Color
    get() = when (this) {
        Difficulty.EASY -> Color(0xFF1CA66B)
        Difficulty.MEDIUM -> Color(0xFFE0A100)
        Difficulty.HARD -> Color(0xFFE2463B)
    }
