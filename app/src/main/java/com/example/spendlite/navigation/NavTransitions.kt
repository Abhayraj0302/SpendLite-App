package com.example.spendlite.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.slideOutVertically

object NavTransitions {
    const val DURATION_MS = 300
    const val SCALE_START = 0.96f
    const val OFFSET_FRACTION = 3

    val EnterEasing: Easing = FastOutSlowInEasing
    val ExitEasing: Easing = FastOutLinearInEasing
    val PopEnterEasing: Easing = LinearOutSlowInEasing

    val slideInRight: EnterTransition = slideInHorizontally(
        initialOffsetX = { fullWidth -> fullWidth / OFFSET_FRACTION },
        animationSpec = tween(durationMillis = DURATION_MS, easing = EnterEasing)
    ) + fadeIn(animationSpec = tween(durationMillis = DURATION_MS, easing = EnterEasing))

    val slideOutLeft: ExitTransition = slideOutHorizontally(
        targetOffsetX = { fullWidth -> -fullWidth / OFFSET_FRACTION },
        animationSpec = tween(durationMillis = DURATION_MS, easing = ExitEasing)
    ) + fadeOut(animationSpec = tween(durationMillis = DURATION_MS, easing = ExitEasing))

    val slideInLeft: EnterTransition = slideInHorizontally(
        initialOffsetX = { fullWidth -> -fullWidth / OFFSET_FRACTION },
        animationSpec = tween(durationMillis = DURATION_MS, easing = PopEnterEasing)
    ) + fadeIn(animationSpec = tween(durationMillis = DURATION_MS, easing = PopEnterEasing))

    val slideOutRight: ExitTransition = slideOutHorizontally(
        targetOffsetX = { fullWidth -> fullWidth / OFFSET_FRACTION },
        animationSpec = tween(durationMillis = DURATION_MS, easing = ExitEasing)
    ) + fadeOut(animationSpec = tween(durationMillis = DURATION_MS, easing = ExitEasing))

    val slideInBottom: EnterTransition = slideInVertically(
        initialOffsetY = { fullHeight -> fullHeight / OFFSET_FRACTION },
        animationSpec = tween(durationMillis = DURATION_MS, easing = EnterEasing)
    ) + fadeIn(animationSpec = tween(durationMillis = DURATION_MS, easing = EnterEasing))

    val slideOutBottom: ExitTransition = slideOutVertically(
        targetOffsetY = { fullHeight -> fullHeight / OFFSET_FRACTION },
        animationSpec = tween(durationMillis = DURATION_MS, easing = ExitEasing)
    ) + fadeOut(animationSpec = tween(durationMillis = DURATION_MS, easing = ExitEasing))

    val fadeThroughIn: EnterTransition = fadeIn(
        animationSpec = tween(durationMillis = DURATION_MS, easing = EnterEasing)
    ) + scaleIn(
        initialScale = SCALE_START,
        animationSpec = tween(durationMillis = DURATION_MS, easing = EnterEasing)
    )

    val fadeThroughOut: ExitTransition = fadeOut(
        animationSpec = tween(durationMillis = DURATION_MS, easing = ExitEasing)
    )
}
