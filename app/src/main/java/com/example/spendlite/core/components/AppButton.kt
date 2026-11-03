package com.example.spendlite.core.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendlite.ui.theme.Motion
import com.example.spendlite.ui.theme.TealAccent

@Composable
fun AppButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    isLoading: Boolean = false,
    loadingDescription: String = "Loading",
    shape: Shape = ButtonDefaults.shape,
    containerColor: Color = TealAccent,
    contentColor: Color = Color.Black,
    fontSize: TextUnit = 16.sp,
    fontWeight: FontWeight = FontWeight.Bold
) {
    val context = LocalContext.current
    val animDisabled = Motion.isAnimationDisabled(context)

    val animatedBgColor by animateColorAsState(
        targetValue = if (enabled && !isLoading) containerColor else containerColor.copy(alpha = 0.4f),
        animationSpec = tween(durationMillis = if (animDisabled) 0 else Motion.DURATION_SHORT),
        label = "btnBgColor"
    )

    Button(
        onClick = {
            if (!isLoading && enabled) {
                onClick()
            }
        },
        enabled = enabled && !isLoading,
        modifier = modifier,
        shape = shape,
        colors = ButtonDefaults.buttonColors(
            containerColor = animatedBgColor,
            contentColor = contentColor,
            disabledContainerColor = containerColor.copy(alpha = 0.4f),
            disabledContentColor = contentColor.copy(alpha = 0.4f)
        )
    ) {
        Box(
            contentAlignment = Alignment.Center
        ) {
            AnimatedContent(
                targetState = isLoading,
                transitionSpec = {
                    val duration = if (animDisabled) 0 else Motion.DURATION_SHORT
                    fadeIn(animationSpec = tween(duration)) togetherWith
                            fadeOut(animationSpec = tween(duration))
                },
                label = "btnContent"
            ) { loading ->
                if (loading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .semantics { contentDescription = loadingDescription },
                        color = contentColor,
                        strokeWidth = 2.dp
                    )
                } else {
                    Text(text, fontSize = fontSize, fontWeight = fontWeight)
                }
            }
        }
    }
}
