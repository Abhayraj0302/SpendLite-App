package com.example.spendlite.AppUI

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.SwipeToDismissBox
import androidx.compose.material3.SwipeToDismissBoxValue
import androidx.compose.material3.Text
import androidx.compose.material3.rememberSwipeToDismissBoxState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.spendlite.R
import com.example.spendlite.ViewModelFiles.Expense
import com.example.spendlite.ViewModelFiles.ViewModelApp
import com.example.spendlite.ui.theme.AppBackground
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TealTint
import com.example.spendlite.ui.theme.TextMuted
import com.example.spendlite.ui.theme.TextPrimary
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun MainScreen(
    navController: NavHostController,
    vm: ViewModelApp = viewModel()
) {
    var MenuExpanded by remember { mutableStateOf(false) }
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(AppBackground)
                .padding(top = 40.dp, start = 16.dp, end = 16.dp)
        ) {

            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(horizontal = 0.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
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
                        }
                        Box(
                            contentAlignment = Alignment.TopEnd,
                            modifier = Modifier.background(Color.Transparent)
                        ) {

                            IconButton(onClick = {
                                MenuExpanded = !MenuExpanded
                            }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = null,
                                    tint = TealAccent
                                )
                            }

                            DropdownMenu(
                                expanded = MenuExpanded,
                                onDismissRequest = { MenuExpanded = false },
                                modifier = Modifier
                                    .background(
                                        color = Color(0xFF1E1E1E),
                                        shape = RoundedCornerShape(12.dp)
                                    )
                                    .width(170.dp)
                                    .padding(0.dp),
                                shape = RoundedCornerShape(12.dp),
                                containerColor = Color(0xFF1E1E1E)
                            ) {
                                DropdownMenuItem(
                                    contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
                                    leadingIcon = {
                                        Icon(
                                            painter = painterResource(R.drawable.outline_logout_24),
                                            contentDescription = null,
                                            tint = Color(0xFFFF6B6B),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    },
                                    text = {
                                        Text(
                                            "Sign Out",
                                            color = Color.White,
                                            fontSize = 16.sp,
                                            fontWeight = FontWeight.Medium
                                        )
                                    },
                                    onClick = {
                                        MenuExpanded = false
                                        Firebase.auth.signOut()
                                        navController.navigate("SignInPage") {
                                            popUpTo("home") { inclusive = true }
                                        }
                                    },
                                    colors = MenuDefaults.itemColors(
                                        textColor = Color.White,
                                        leadingIconColor = Color.White,
                                    )
                                )
                            }
                        }
                    }


                    val filters = listOf("All", "Income", "Expense", "Pending")
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        filters.forEach { filter ->
                            FilterChip(
                                selected = vm.selectedFilter == filter,
                                onClick = { vm.onFilterSelected(filter) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TealTint,
                                    selectedLabelColor = TealAccent,
                                    containerColor = Color.Transparent,
                                    labelColor = TextMuted
                                ),
                                label = { Text(filter) },
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = vm.selectedFilter == filter,
                                    borderColor = Color.Transparent,
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
                items(vm.filteredExpenses, key = { it.title + it.date + it.amount }) { expense ->
                    SwipeToDeleteExpenseCard(
                        expense = expense,
                        onDelete = { vm.deleteExpense(expense) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }


            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        FloatingActionButton(
            onClick = { navController.navigate("add") },
            containerColor = TealAccent,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeToDeleteExpenseCard(
    expense: Expense,
    onDelete: () -> Unit
) {
    val dismissState = rememberSwipeToDismissBoxState(
        confirmValueChange = { value ->
            if (value == SwipeToDismissBoxValue.EndToStart) {
                onDelete()
                true
            } else false
        },
        positionalThreshold = { it * 0.4f }
    )

    SwipeToDismissBox(
        state = dismissState,
        enableDismissFromStartToEnd = false,
        enableDismissFromEndToStart = true,
        backgroundContent = {
            val color by animateColorAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                    Color(0xFFB00020) else Color(0xFF2A2A2A),
                label = "swipe bg"
            )
            val scale by animateFloatAsState(
                targetValue = if (dismissState.targetValue == SwipeToDismissBoxValue.EndToStart)
                    1f else 0.75f,
                label = "icon scale"
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(color, RoundedCornerShape(16.dp))
                    .padding(end = 20.dp),
                contentAlignment = Alignment.CenterEnd
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Delete",
                    tint = Color.White,
                    modifier = Modifier.scale(scale)
                )
            }
        }
    ) {
        ExpenseCard(expense)
    }
}

@Composable
fun ExpenseCard(expense: Expense) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF1A1A1A))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = expense.title,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = expense.date,
                    color = TextMuted,
                    fontSize = 13.sp
                )
            }
            Text(
                text = "-₹${
                    java.text.NumberFormat
                        .getNumberInstance(java.util.Locale.US)
                        .format(expense.amount.toInt())
                }",
                color = Color(0xFFFF6B6B),
                fontSize = 16.sp,
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