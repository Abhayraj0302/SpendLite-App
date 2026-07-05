package com.example.spendlite.features.home.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TextMuted

@Composable
fun BalanceCard(
    totalCount: String,
    totalCountValue: Int
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
        Row(modifier = androidx.compose.ui.Modifier.padding(vertical = 2.dp)) {
            Icon(
                modifier = androidx.compose.ui.Modifier.size(18.dp),
                imageVector = Icons.Filled.ArrowDownward,
                contentDescription = null,
                tint = TealAccent
            )
            Row {
                Text("8% ", color = TealAccent, fontSize = 16.sp)
                Text("from last month", color = TextMuted, fontSize = 16.sp)
            }
        }
    }
}