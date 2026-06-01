package com.example.spendlite.AppUI

import android.widget.Toast
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign.Companion.Center
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.spendlite.ViewModelFiles.ViewModelApp
import com.example.spendlite.ui.theme.AppBackground
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TealTint
import com.example.spendlite.ui.theme.TextDim
import com.example.spendlite.ui.theme.TextMuted
import com.example.spendlite.ui.theme.TextPrimary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    vm: ViewModelApp = viewModel(),
    navController: NavHostController,
) {

    var amount by rememberSaveable { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }
    var selectedDate by remember { mutableStateOf("Select Date") }
    val context = LocalContext.current

    Scaffold(
        topBar = {
            TopAppBar(
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = AppBackground,
                    titleContentColor = TextPrimary,
                    navigationIconContentColor = TextPrimary
                ),
                title = {
                    Text(
                        "add expense",
                        fontSize = 30.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = null
                        )
                    }
                }
            )
        },
        containerColor = AppBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .fillMaxWidth()
                .pointerInput(Unit) {
                    detectHorizontalDragGestures { _, dragAmount ->
                        if (dragAmount < -50) {
                            navController.popBackStack()
                        }
                    }
                },
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text("amount", color = TextMuted, fontSize = 20.sp, fontWeight = FontWeight.Bold)

            Spacer(modifier = Modifier.height(8.dp))

            BasicTextField(
                value = amount,
                onValueChange = { input ->
                    if (input.matches(Regex("^\\d*\\.?\\d{0,2}$"))) {
                        amount = input
                    }
                },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                textStyle = TextStyle(
                    fontSize = 48.sp,
                    color = TextPrimary,
                    textAlign = Center
                ),
                cursorBrush = SolidColor(TealAccent),
                decorationBox = { innerTextField ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(text = "₹", fontSize = 32.sp, color = TextMuted)
                        Spacer(modifier = Modifier.width(4.dp))
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier.width(IntrinsicSize.Min)
                        ) {

                            if (amount.isEmpty()) {
                                Text(
                                    text = "0.00",
                                    fontSize = 48.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary,
                                    textAlign = Center
                                )
                            }
                            innerTextField()
                        }
                    }
                }
            )

            HorizontalDivider(
                modifier = Modifier.padding(horizontal = 100.dp, vertical = 8.dp),
                thickness = 4.dp,
                color = TealAccent
            )

            TextField(
                value = vm.newspends,
                onValueChange = { vm.updateNewSpends(it) },
                singleLine = true,

                textStyle = TextStyle(
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),

                placeholder = {
                    Column {
                        Text(
                            "Title",
                            fontWeight = FontWeight.Bold,
                            color = TextDim,
                            fontSize = 16.sp
                        )
                        Text(
                            "e.g. Coffee,Rent...",
                            fontWeight = FontWeight.Bold,
                            color = TextDim,
                            fontSize = 16.sp
                        )
                    }
                },
                shape = RoundedCornerShape(20.dp),

                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFF1E1E1E),
                    unfocusedContainerColor = Color(0xFF1E1E1E),

                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,

                    cursorColor = TealAccent,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White
                ),

                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 12.dp)
                    .height(85.dp)
            )

            Card(
                modifier = Modifier
                    .height(85.dp)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E1E),
                    contentColor = Color.White
                )
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    Column {

                        Text(
                            "Category",
                            fontWeight = FontWeight.Bold,
                            color = TextDim,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        val filters = listOf("Food", "Travel", "Bills", "Other")
                        val selected = vm.selectexpenseCategory

                        Row(
                            horizontalArrangement = Arrangement.SpaceAround,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            filters.forEach { filter ->
                                FilterChip(
                                    selected = selected == filter,
                                    onClick = { vm.onCategorySelect(filter) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = TealTint,
                                        selectedLabelColor = TealAccent,
                                        containerColor = Color.Transparent,
                                        labelColor = TextMuted
                                    ),
                                    label = { Text(filter) },
                                    border = FilterChipDefaults.filterChipBorder(
                                        enabled = true,
                                        selected = selected == filter,
                                        borderColor = Color.Transparent,
                                        selectedBorderColor = Color.Transparent
                                    )
                                )
                            }
                        }
                    }
                }
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),

                shape = RoundedCornerShape(20.dp),

                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFF1E1E1E)
                ),

                onClick = {
                    showDatePicker = true
                }
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {

                        Text(
                            "Date",
                            color = TextDim,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        Text(
                            selectedDate,
                            color = TextPrimary,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = Color.White
                    )
                }
            }
            if (showDatePicker) {

                val datePickerState = rememberDatePickerState()

                DatePickerDialog(
                    onDismissRequest = { showDatePicker = false },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                val millis = datePickerState.selectedDateMillis
                                if (millis != null) {
                                    val formatter =
                                        SimpleDateFormat("dd MMM yyyy", Locale.getDefault())
                                    selectedDate = formatter.format(Date(millis))
                                }
                                showDatePicker = false
                            }
                        ) {
                            Text("OK", color = TealAccent)
                        }
                    },
                    dismissButton = {
                        TextButton(onClick = { showDatePicker = false }) {
                            Text("Cancel", color = TextMuted)
                        }
                    }
                ) {
                    DatePicker(state = datePickerState)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    val parsed = amount.toDoubleOrNull()
                    if (parsed != null && parsed > 0 && vm.newspends.isNotBlank()) {
                        vm.addExpense(
                            title    = vm.newspends,
                            amount   = parsed,
                            category = vm.selectexpenseCategory,
                            date     = selectedDate
                        )
                        Toast.makeText(context, "Expense Added!", Toast.LENGTH_SHORT).show()
                        navController.popBackStack()
                    }else {
                        Toast.makeText(
                            context,
                            "Please Enter Amount, Title and Date",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp),
                shape = RoundedCornerShape(20.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = TealAccent,
                    contentColor   = Color.Black
                )
            ) {
                Text("Save Expense", fontSize = 22.sp, fontWeight = FontWeight.Bold)
            }

        }

    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AddScreenPreview() {
    AddScreen(navController = rememberNavController())
}