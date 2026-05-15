package com.example.spendlite.AppUI

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.spendlite.ViewModelFiles.Expense
import com.example.spendlite.ViewModelFiles.ViewModelApp
import com.example.spendlite.ui.theme.*

@Composable
fun MainScreen(
    navController: NavHostController,
    vm: ViewModelApp = viewModel()
) {
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
        ) {

            item {
                Column {
                    Text(
                        "this month",
                        fontSize = 20.sp,
                        color = TextMuted,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        "₹${vm.totalCount}",
                        fontSize = 40.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Row(modifier = Modifier.padding(vertical = 2.dp)) {
                        Icon(
                            modifier = Modifier.size(18.dp),
                            imageVector = Icons.Filled.ArrowDownward,
                            contentDescription = null,
                            tint = TealAccent
                        )
                        Text("8% from last month", color = TealAccent, fontSize = 16.sp)
                    }


                    val filters = listOf("All", "Income", "Expense", "Pending")
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        filters.forEach { filter ->
                            FilterChip(
                                selected = vm.selectedFilter == filter,
                                onClick  = { vm.onFilterSelected(filter) },
                                colors   = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TealTint,
                                    selectedLabelColor     = TealAccent,
                                    containerColor         = Color.Transparent,
                                    labelColor             = TextMuted
                                ),
                                label  = { Text(filter) },
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled             = true,
                                    selected            = vm.selectedFilter == filter,
                                    borderColor         = Color.Transparent,
                                    selectedBorderColor = Color.Transparent
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }


            if (vm.filteredExpenses.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            "No expenses yet",
                            color = TextMuted,
                            fontSize = 16.sp
                        )
                    }
                }
            } else {
                items(vm.filteredExpenses) { expense ->
                    ExpenseCard(expense)
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }


            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        FloatingActionButton(
            onClick         = { navController.navigate("add") },
            containerColor  = TealAccent,
            modifier        = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}


@Composable
fun ExpenseCard(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape    = RoundedCornerShape(16.dp),
        colors   = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier            = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment   = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text       = expense.title,
                    color      = Color.White,
                    fontSize   = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text     = expense.date,
                    color    = TextMuted,
                    fontSize = 13.sp
                )
            }
            Text(
                text       = "-₹${
                    java.text.NumberFormat
                        .getNumberInstance(java.util.Locale.US)
                        .format(expense.amount.toInt())
                }",
                color      = Color(0xFFFF6B6B),
                fontSize   = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun MainScreenPreview() {
    MainScreen(navController = rememberNavController())
}