package com.example.spendlite.features.home

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
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.spendlite.navigation.NavRoutes
import com.example.spendlite.R
import com.example.spendlite.data.remote.FirebaseService
import com.example.spendlite.features.home.components.BalanceCard
import com.example.spendlite.features.home.components.SwipeToDeleteExpenseCard
import com.example.spendlite.ui.theme.AppBackground
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TealTint
import com.example.spendlite.ui.theme.TextMuted

@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: HomeViewModel = viewModel()
) {
    val state = vm.state
    var menuExpanded by remember { mutableStateOf(false) }

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
                        BalanceCard(
                            totalCount = state.totalCount,
                            totalCountValue = state.totalCountValue
                        )

                        Box(
                            contentAlignment = Alignment.TopEnd,
                            modifier = Modifier.background(Color.Transparent)
                        ) {
                            IconButton(onClick = { menuExpanded = !menuExpanded }) {
                                Icon(
                                    Icons.Default.MoreVert,
                                    contentDescription = null,
                                    tint = TealAccent
                                )
                            }

                            DropdownMenu(
                                expanded = menuExpanded,
                                onDismissRequest = { menuExpanded = false },
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
                                    },onClick = {
                                        menuExpanded = false
                                        FirebaseService.signOut()
                                        navController.navigate(NavRoutes.SIGN_IN) {
                                            popUpTo(NavRoutes.HOME) { inclusive = true }
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

                    val filters = listOf("All", "Income", "Expense")
                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        filters.forEach { filter ->
                            FilterChip(
                                selected = state.selectedFilter == filter,
                                onClick = { vm.onEvent(HomeEvent.OnFilterSelected(filter)) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = TealTint,
                                    selectedLabelColor = TealAccent,
                                    containerColor = Color.Transparent,
                                    labelColor = TextMuted
                                ),
                                label = { Text(filter) },
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = state.selectedFilter == filter,
                                    borderColor = Color.Transparent,
                                    selectedBorderColor = Color.Transparent
                                )
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                }
            }

            if (state.filteredExpenses.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("No expenses yet", color = TextMuted, fontSize = 16.sp)
                    }
                }
            } else {
                items(state.filteredExpenses, key = { it.title + it.date + it.amount }) { expense ->
                    SwipeToDeleteExpenseCard(
                        expense = expense,
                        onDelete = { vm.onEvent(HomeEvent.OnDeleteExpense(expense)) }
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        FloatingActionButton(
            onClick = { navController.navigate(NavRoutes.ADD) },
            containerColor = TealAccent,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun HomeScreenPreview() {
    HomeScreen(navController = rememberNavController())
}