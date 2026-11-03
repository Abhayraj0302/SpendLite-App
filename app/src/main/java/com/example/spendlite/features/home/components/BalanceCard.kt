package com.example.spendlite.features.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendlite.ui.theme.Motion
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TextMuted
import java.text.NumberFormat
import java.util.Locale
import kotlin.math.abs

@Composable
fun BalanceCard(
    totalCount: String,
    totalCountValue: Int,
    monthComparisonPercent: Int?,
    spendingIncreased: Boolean
) {
    val context = LocalContext.current
    val animDisabled = Motion.isAnimationDisabled(context)

    val animatedValue by animateIntAsState(
        targetValue = totalCountValue,
        animationSpec = tween(
            durationMillis = if (animDisabled) 0 else Motion.DURATION_LONG,
            easing = Motion.EasingEmphasized
        ),
        label = "balanceValue"
    )

    val animatedColor by animateColorAsState(
        targetValue = when {
            totalCountValue < 0 -> Color(0xFFFF6B6B)
            totalCountValue == 0 -> Color.White
            else -> TealAccent
        },
        animationSpec = tween(
            durationMillis = if (animDisabled) 0 else Motion.DURATION_MEDIUM,
            easing = Motion.EasingEmphasized
        ),
        label = "balanceColor"
    )

    val formattedAmount = NumberFormat.getNumberInstance(Locale.US).format(animatedValue)

    Column {
        Text(
            "this month",
            fontSize = 20.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
        )
        Text(
            "₹$formattedAmount",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = animatedColor
        )

        AnimatedContent(
            targetState = monthComparisonPercent to spendingIncreased,
            transitionSpec = {
                val duration = if (animDisabled) 0 else Motion.DURATION_SHORT
                (slideInVertically(animationSpec = tween(duration)) { height -> height } + fadeIn(animationSpec = tween(duration))) togetherWith
                        (slideOutVertically(animationSpec = tween(duration)) { height -> -height } + fadeOut(animationSpec = tween(duration)))
            },
            label = "monthComparison"
        ) { (percent, increased) ->
            if (percent == null) {
                Text(
                    "No expenses last month to compare",
                    color = TextMuted,
                    fontSize = 14.sp,
                    modifier = Modifier.padding(vertical = 2.dp)
                )
            } else {
                val changeColor = if (increased) Color(0xFFFF6B6B) else TealAccent
                Row(
                    modifier = Modifier.padding(vertical = 2.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        modifier = Modifier.size(18.dp),
                        imageVector = if (increased) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                        contentDescription = null,
                        tint = changeColor
                    )
                    Row {
                        Text("${abs(percent)}% ", color = changeColor, fontSize = 16.sp)
                        Text("from last month", color = TextMuted, fontSize = 16.sp)
                    }
                }
            }
        }
    }
}
