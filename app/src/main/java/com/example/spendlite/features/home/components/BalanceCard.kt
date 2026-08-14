package com.example.spendlite.features.home.components

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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TextMuted
import kotlin.math.abs

@Composable
fun BalanceCard(
    totalCount: String,
    totalCountValue: Int,
    monthComparisonPercent: Int?,
    spendingIncreased: Boolean
) {
    Column {
        Text(
            "this month",
            fontSize = 20.sp,
            color = TextMuted,
            fontWeight = FontWeight.Medium
        )
        Text(
            "₹$totalCount",
            fontSize = 40.sp,
            fontWeight = FontWeight.Bold,
            color = when {
                totalCountValue < 0 -> Color(0xFFFF6B6B)
                totalCountValue == 0 -> Color.White
                else -> TealAccent
            }
        )
        if (monthComparisonPercent == null) {
            Text(
                "No expenses last month to compare",
                color = TextMuted,
                fontSize = 14.sp,
                modifier = androidx.compose.ui.Modifier.padding(vertical = 2.dp)
            )
        } else {
            val changeColor = if (spendingIncreased) Color(0xFFFF6B6B) else TealAccent
            Row(modifier = androidx.compose.ui.Modifier.padding(vertical = 2.dp)) {
                Icon(
                    modifier = androidx.compose.ui.Modifier.size(18.dp),
                    imageVector = if (spendingIncreased) Icons.Filled.ArrowUpward else Icons.Filled.ArrowDownward,
                    contentDescription = null,
                    tint = changeColor
                )
                Row {
                    Text("${abs(monthComparisonPercent)}% ", color = changeColor, fontSize = 16.sp)
                    Text("from last month", color = TextMuted, fontSize = 16.sp)
                }
            }
        }
    }
}