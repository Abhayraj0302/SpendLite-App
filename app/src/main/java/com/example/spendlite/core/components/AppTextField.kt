package com.example.spendlite.core.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp
import com.example.spendlite.R
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TealTint

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIconRes: Int,
    modifier: Modifier = Modifier,
    keyboardType: KeyboardType = KeyboardType.Text,
    isError: Boolean = false,
    errorText: String? = null,
    fontSize: TextUnit = 16.sp
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier.fillMaxWidth(0.8f),
        value = value,
        onValueChange = onValueChange,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TealAccent,
            unfocusedBorderColor = TealTint,
            errorBorderColor = Color.Red
        ),
        placeholder = { Text(placeholder, color = Color(0xFF718096)) },
        leadingIcon = {
            Icon(
                painter = painterResource(leadingIconRes),
                contentDescription = null,
                tint = Color(0xFF5DCAA5)
            )
        },
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        textStyle = TextStyle(color = Color.White, fontSize = fontSize, fontWeight = FontWeight.Bold),
        isError = isError,
        supportingText = {
            if (errorText != null) {
                Text(text = errorText, color = Color.Red, fontSize = 12.sp)
            }
        }
    )
}

@Composable
fun AppPasswordField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    passwordVisible: Boolean,
    onVisibilityToggle: () -> Unit,
    modifier: Modifier = Modifier,
    fontSize: TextUnit = 14.sp
) {
    OutlinedTextField(
        singleLine = true,
        modifier = modifier.fillMaxWidth(0.8f),
        value = value,
        onValueChange = onValueChange,
        colors = OutlinedTextFieldDefaults.colors(
            focusedBorderColor = TealAccent,
            unfocusedBorderColor = TealTint,
            errorBorderColor = Color.Red
        ),
        placeholder = { Text(placeholder, color = Color(0xFF718096)) },
        visualTransformation =
            if (passwordVisible) VisualTransformation.None
            else PasswordVisualTransformation(),
        leadingIcon = {
            Icon(
                painter = painterResource(R.drawable.outline_lock_24),
                contentDescription = null,
                tint = Color(0xFF5DCAA5)
            )
        },
        trailingIcon = {
            IconButton(onClick = onVisibilityToggle) {
                Icon(
                    painter =
                        if (passwordVisible) painterResource(R.drawable.outline_visibility_24)
                        else painterResource(R.drawable.outline_visibility_off_24),
                    contentDescription = null,
                    tint = Color(0xFF5DCAA5)
                )
            }
        },
        textStyle = TextStyle(color = Color.White, fontSize = fontSize, fontWeight = FontWeight.Bold)
    )
}