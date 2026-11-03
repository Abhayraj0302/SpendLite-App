package com.example.spendlite.core.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.spendlite.R
import com.example.spendlite.ui.theme.Motion
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TealTint

@Composable
fun AppTextField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingIconRes: Int,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    keyboardType: KeyboardType = KeyboardType.Text,
    imeAction: ImeAction = ImeAction.Next,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorText: String? = null,
    errorTick: Int = 0,
    fontSize: TextUnit = 16.sp
) {
    val context = LocalContext.current
    val shakeOffsetX = remember { Animatable(0f) }

    LaunchedEffect(errorTick) {
        if (errorTick > 0 && !Motion.isAnimationDisabled(context)) {
            shakeOffsetX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 350
                    0.0f at 0
                    -10f at 50
                    10f at 100
                    -6f at 175
                    6f at 250
                    0.0f at 350
                }
            )
        }
    }

    OutlinedTextField(
        singleLine = true,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth(0.8f)
            .graphicsLayer { translationX = shakeOffsetX.value },
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
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType, imeAction = imeAction),
        keyboardActions = keyboardActions,
        textStyle = TextStyle(color = Color.White, fontSize = fontSize, fontWeight = FontWeight.Bold),
        isError = isError,
        supportingText = {
            AnimatedVisibility(
                visible = errorText != null,
                enter = fadeIn(animationSpec = tween(if (Motion.isAnimationDisabled(context)) 0 else Motion.DURATION_SHORT)) + expandVertically(),
                exit = fadeOut(animationSpec = tween(if (Motion.isAnimationDisabled(context)) 0 else Motion.DURATION_SHORT)) + shrinkVertically()
            ) {
                if (errorText != null) {
                    Text(text = errorText, color = Color.Red, fontSize = 12.sp)
                }
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
    enabled: Boolean = true,
    imeAction: ImeAction = ImeAction.Done,
    keyboardActions: KeyboardActions = KeyboardActions.Default,
    isError: Boolean = false,
    errorText: String? = null,
    errorTick: Int = 0,
    fontSize: TextUnit = 14.sp
) {
    val context = LocalContext.current
    val shakeOffsetX = remember { Animatable(0f) }

    LaunchedEffect(errorTick) {
        if (errorTick > 0 && !Motion.isAnimationDisabled(context)) {
            shakeOffsetX.animateTo(
                targetValue = 0f,
                animationSpec = keyframes {
                    durationMillis = 350
                    0.0f at 0
                    -10f at 50
                    10f at 100
                    -6f at 175
                    6f at 250
                    0.0f at 350
                }
            )
        }
    }

    OutlinedTextField(
        singleLine = true,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth(0.8f)
            .graphicsLayer { translationX = shakeOffsetX.value },
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
            IconButton(onClick = onVisibilityToggle, enabled = enabled) {
                AnimatedContent(
                    targetState = passwordVisible,
                    transitionSpec = {
                        val duration = if (Motion.isAnimationDisabled(context)) 0 else Motion.DURATION_SHORT
                        fadeIn(animationSpec = tween(duration)) togetherWith
                                fadeOut(animationSpec = tween(duration))
                    },
                    label = "passwordIconCrossfade"
                ) { visible ->
                    Icon(
                        painter =
                            if (visible) painterResource(R.drawable.outline_visibility_24)
                            else painterResource(R.drawable.outline_visibility_off_24),
                        contentDescription = null,
                        tint = Color(0xFF5DCAA5)
                    )
                }
            }
        },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = imeAction),
        keyboardActions = keyboardActions,
        textStyle = TextStyle(color = Color.White, fontSize = fontSize, fontWeight = FontWeight.Bold),
        isError = isError,
        supportingText = {
            AnimatedVisibility(
                visible = errorText != null,
                enter = fadeIn(animationSpec = tween(if (Motion.isAnimationDisabled(context)) 0 else Motion.DURATION_SHORT)) + expandVertically(),
                exit = fadeOut(animationSpec = tween(if (Motion.isAnimationDisabled(context)) 0 else Motion.DURATION_SHORT)) + shrinkVertically()
            ) {
                if (errorText != null) {
                    Text(text = errorText, color = Color.Red, fontSize = 12.sp)
                }
            }
        }
    )
}
