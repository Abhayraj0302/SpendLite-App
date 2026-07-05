package com.example.spendlite.features.auth.signup

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.spendlite.navigation.NavRoutes
import com.example.spendlite.R
import com.example.spendlite.core.components.AppButton
import com.example.spendlite.core.components.AppPasswordField
import com.example.spendlite.core.components.AppTextField
import com.example.spendlite.ui.theme.AppBackground
import com.example.spendlite.ui.theme.TealAccent

@Composable
fun SignUpScreen(
    vm: SignUpViewModel = viewModel(),
    navController: NavHostController,
) {
    val state = vm.state
    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(AppBackground),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row {
            Text("Spend", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = Color.LightGray)
            Text("Lite", fontSize = 30.sp, fontWeight = FontWeight.Bold, color = TealAccent)
        }

        Spacer(modifier = Modifier.padding(top = 32.dp).let { Modifier })
        Spacer(modifier = Modifier.padding(top = 32.dp))

        AppTextField(
            value = state.email,
            onValueChange = { vm.onEvent(SignUpEvent.OnEmailChanged(it)) },
            placeholder = "Enter your gmail",
            leadingIconRes = R.drawable.outline_email_24,
            keyboardType = KeyboardType.Email,
            isError = state.emailError != null,
            errorText = state.emailError
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

        AppPasswordField(
            value = state.password,
            onValueChange = { vm.onEvent(SignUpEvent.OnPasswordChanged(it)) },
            placeholder = "Create your Password",
            passwordVisible = state.passwordVisible,
            onVisibilityToggle = { vm.onEvent(SignUpEvent.OnTogglePasswordVisibility) }
        )

        Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))

        AppButton(
            text = "Create Account",
            modifier = Modifier.fillMaxWidth(0.75f),
            onClick = {
                vm.onEvent(SignUpEvent.OnSignUpClick(onResult = { success ->
                    if (success) {
                        Toast.makeText(context, "Sign up Successful", Toast.LENGTH_SHORT).show()
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.SIGN_IN) { inclusive = true }
                        }
                    }
                }))
            }
        )

        TextButton(onClick = { }) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.googleimage), contentDescription = null,
                    modifier = Modifier.size(17.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    "Sign up using Google",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = TealAccent,
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