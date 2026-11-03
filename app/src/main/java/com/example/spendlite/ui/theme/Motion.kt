package com.example.spendlite.ui.theme

import android.content.Context
import android.provider.Settings
import androidx.compose.animation.core.AnimationSpec
import androidx.compose.animation.core.Easing
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.SpringSpec
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext

object Motion {
    const val DURATION_SHORT = 150
    const val DURATION_MEDIUM = 300
    const val DURATION_LONG = 500

    val EasingEmphasized: Easing = FastOutSlowInEasing
    val EasingDecelerate: Easing = LinearOutSlowInEasing
    val EasingAccelerate: Easing = FastOutLinearInEasing

    val BouncySpring: SpringSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioMediumBouncy,
        stiffness = Spring.StiffnessLow
    )

    val GentleSpring: SpringSpec<Float> = spring(
        dampingRatio = Spring.DampingRatioNoBouncy,
        stiffness = Spring.StiffnessLow
    )

    fun isAnimationDisabled(context: Context): Boolean {
        return try {
            val scale = Settings.Global.getFloat(
                context.contentResolver,
                Settings.Global.ANIMATOR_DURATION_SCALE,
                1.0f
            )
            scale == 0f
        } catch (e: Exception) {
            false
        }
    }

    @Composable
    fun <T> rememberSpec(spec: AnimationSpec<T>): AnimationSpec<T> {
        val context = LocalContext.current
        return remember(context, spec) {
            if (isAnimationDisabled(context)) tween(durationMillis = 0) else spec
        }
    }

    @Composable
    fun rememberDuration(durationMillis: Int): Int {
        val context = LocalContext.current
        return remember(context, durationMillis) {
            if (isAnimationDisabled(context)) 0 else durationMillis
        }
    }
}
