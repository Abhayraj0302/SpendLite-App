package com.example.spendlite.features.home

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spendlite.navigation.NavRoutes
import com.example.spendlite.R
import com.example.spendlite.data.remote.FirebaseService
import com.example.spendlite.features.home.components.BalanceCard
import com.example.spendlite.features.home.components.SwipeToDeleteExpenseCard
import com.example.spendlite.ui.theme.AppBackground
import com.example.spendlite.ui.theme.Motion
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TealTint
import com.example.spendlite.ui.theme.TextMuted

private val FILTERS = listOf("All", "Income", "Expense")

@Composable
fun HomeScreen(
    navController: NavHostController,
    vm: HomeViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsState()
    var menuExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current
    val animDisabled = Motion.isAnimationDisabled(context)

    var fabEntered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        fabEntered = true
    }

    val fabInteractionSource = remember { MutableInteractionSource() }
    val isFabPressed by fabInteractionSource.collectIsPressedAsState()

    val fabScale by animateFloatAsState(
        targetValue = when {
            !fabEntered -> 0f
            isFabPressed -> 0.95f
            else -> 1f
        },
        animationSpec = Motion.BouncySpring,
        label = "fabScale"
    )

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
                            totalCountValue = state.totalCountValue,
                            monthComparisonPercent = state.monthComparisonPercent,
                            spendingIncreased = state.spendingIncreased
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
                                    }, onClick = {
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

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        FILTERS.forEach { filter ->
                            val isSelected = state.selectedFilter == filter
                            val interactionSource = remember { MutableInteractionSource() }
                            val isPressed by interactionSource.collectIsPressedAsState()

                            val chipScale by animateFloatAsState(
                                targetValue = if (isPressed) 0.95f else (if (isSelected) 1.05f else 1.0f),
                                animationSpec = Motion.BouncySpring,
                                label = "chipScale"
                            )

                            val animBgColor by animateColorAsState(
                                targetValue = if (isSelected) TealTint else Color.Transparent,
                                animationSpec = tween(if (animDisabled) 0 else Motion.DURATION_SHORT),
                                label = "chipBgColor"
                            )

                            val animTextColor by animateColorAsState(
                                targetValue = if (isSelected) TealAccent else TextMuted,
                                animationSpec = tween(if (animDisabled) 0 else Motion.DURATION_SHORT),
                                label = "chipTextColor"
                            )

                            FilterChip(
                                selected = isSelected,
                                onClick = { vm.onEvent(HomeEvent.OnFilterSelected(filter)) },
                                interactionSource = interactionSource,
                                modifier = Modifier.graphicsLayer {
                                    scaleX = chipScale
                                    scaleY = chipScale
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = animBgColor,
                                    selectedLabelColor = animTextColor,
                                    containerColor = animBgColor,
                                    labelColor = animTextColor
                                ),
                                label = { Text(filter) },
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isSelected,
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
                items(state.filteredExpenses, key = { it.id }) { expense ->
                    Box(modifier = Modifier.animateItem()) {
                        val onDelete = remember(expense) {
                            { vm.onEvent(HomeEvent.OnDeleteExpense(expense)) }
                        }
                        SwipeToDeleteExpenseCard(
                            expense = expense,
                            onDelete = onDelete
                        )
                    }
                    Spacer(modifier = Modifier.height(10.dp))
                }
            }

            item { Spacer(modifier = Modifier.height(80.dp)) }
        }

        FloatingActionButton(
            onClick = { navController.navigate(NavRoutes.ADD) },
            containerColor = TealAccent,
            interactionSource = fabInteractionSource,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .graphicsLayer {
                    scaleX = fabScale
                    scaleY = fabScale
                }
        ) {
            Icon(imageVector = Icons.Default.Add, contentDescription = "Add")
        }
    }
}
