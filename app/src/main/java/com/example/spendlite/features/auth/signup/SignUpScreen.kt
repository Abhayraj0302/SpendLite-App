package com.example.spendlite.features.auth.signup

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.example.spendlite.navigation.NavRoutes
import com.example.spendlite.R
import com.example.spendlite.core.components.AppButton
import com.example.spendlite.core.components.AppPasswordField
import com.example.spendlite.core.components.AppTextField
import com.example.spendlite.core.components.rememberGoogleSignInHandler
import com.example.spendlite.core.utils.AuthSource
import com.example.spendlite.ui.theme.AppBackground
import com.example.spendlite.ui.theme.Motion
import com.example.spendlite.ui.theme.TealAccent

@Composable
fun SignUpScreen(
    vm: SignUpViewModel = hiltViewModel(),
    navController: NavHostController,
) {
    val state = vm.state
    val context = LocalContext.current
    val animDisabled = Motion.isAnimationDisabled(context)

    val focusManager = LocalFocusManager.current
    val keyboardController = LocalSoftwareKeyboardController.current

    BackHandler(enabled = state.isLoading) {}

    var headerEntered by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        headerEntered = true
    }

    val headerOffsetY by animateFloatAsState(
        targetValue = if (headerEntered) 0f else 30f,
        animationSpec = tween(if (animDisabled) 0 else Motion.DURATION_MEDIUM, easing = Motion.EasingEmphasized),
        label = "headerOffsetY"
    )
    val headerAlpha by animateFloatAsState(
        targetValue = if (headerEntered) 1f else 0f,
        animationSpec = tween(if (animDisabled) 0 else Motion.DURATION_MEDIUM, easing = Motion.EasingEmphasized),
        label = "headerAlpha"
    )

    val googleSignInHandler = rememberGoogleSignInHandler(
        onTokenReceived = { idToken ->
            vm.onEvent(
                SignUpEvent.OnGoogleSignInResult(
                    idToken = idToken,
                    onResult = { success, _ ->
                        if (success) {
                            navController.navigate(NavRoutes.HOME) {
                                popUpTo(NavRoutes.SIGN_IN) { inclusive = true }
                            }
                        }
                    }
                )
            )
        },
        onCancelled = { }
    )

    val submitSignUp = remember(vm, navController, focusManager, keyboardController) {
        {
            focusManager.clearFocus()
            keyboardController?.hide()
            vm.onEvent(SignUpEvent.OnSignUpClick(onResult = { success ->
                if (success) {
                    navController.navigate(NavRoutes.HOME) {
                        popUpTo(NavRoutes.SIGN_IN) { inclusive = true }
                    }
                }
            }))
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.graphicsLayer {
                translationY = headerOffsetY
                alpha = headerAlpha
            }
        ) {
            Text("Spend", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.LightGray)
            Text("Lite", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TealAccent)
        }

        Spacer(modifier = Modifier.height(32.dp))

        AppTextField(
            value = state.email,
            onValueChange = { vm.onEvent(SignUpEvent.OnEmailChanged(it)) },
            placeholder = stringResource(R.string.placeholder_email),
            leadingIconRes = R.drawable.outline_email_24,
            enabled = !state.isLoading,
            keyboardType = KeyboardType.Email,
            imeAction = ImeAction.Next,
            isError = state.emailError != null,
            errorText = state.emailError,
            errorTick = state.errorTick
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppPasswordField(
            value = state.password,
            onValueChange = { vm.onEvent(SignUpEvent.OnPasswordChanged(it)) },
            placeholder = stringResource(R.string.placeholder_create_password),
            passwordVisible = state.passwordVisible,
            onVisibilityToggle = { vm.onEvent(SignUpEvent.OnTogglePasswordVisibility) },
            enabled = !state.isLoading,
            imeAction = ImeAction.Done,
            keyboardActions = KeyboardActions(onDone = { submitSignUp() }),
            isError = state.passwordError != null,
            errorText = state.passwordError,
            errorTick = state.errorTick
        )

        val genError = state.generalError
        AnimatedVisibility(
            visible = genError != null,
            enter = fadeIn(animationSpec = tween(if (animDisabled) 0 else Motion.DURATION_SHORT)) + expandVertically(),
            exit = fadeOut(animationSpec = tween(if (animDisabled) 0 else Motion.DURATION_SHORT)) + shrinkVertically()
        ) {
            if (genError != null) {
                Text(
                    text = genError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        AppButton(
            text = stringResource(R.string.btn_create_account),
            modifier = Modifier.fillMaxWidth(0.75f),
            enabled = !state.isLoading,
            isLoading = state.isLoading && state.loadingSource == AuthSource.EMAIL,
            loadingDescription = "Creating account",
            onClick = submitSignUp
        )

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(
            onClick = {
                focusManager.clearFocus()
                keyboardController?.hide()
                googleSignInHandler.launchGoogleSignIn()
            },
            enabled = !state.isLoading
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (state.isLoading && state.loadingSource == AuthSource.GOOGLE) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(17.dp)
                            .semantics { contentDescription = "Signing in with Google" },
                        color = TealAccent,
                        strokeWidth = 2.dp
                    )
                } else {
                    Image(
                        painter = painterResource(R.drawable.googleimage), contentDescription = null,
                        modifier = Modifier.size(17.dp)
                    )
                }
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    stringResource(R.string.btn_google_signup),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = if (state.isLoading) TealAccent.copy(alpha = 0.4f) else TealAccent,
                    textDecoration = TextDecoration.Underline
                )
            }
        }
    }
}

@Preview(showSystemUi = true)
@Composable
fun SignUpScreenPreview() {
    SignUpScreen(navController = NavHostController(LocalContext.current))
}
