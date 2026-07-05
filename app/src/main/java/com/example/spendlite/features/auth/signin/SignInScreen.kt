package com.example.spendlite.features.auth.signin

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
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
import com.example.spendlite.core.utils.AuthConstants
//import com.example.spendlite.navigation.NavRoutes
import com.example.spendlite.ui.theme.AppBackground
import com.example.spendlite.ui.theme.TealAccent
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

@Composable
fun SignInScreen(
    vm: SignInViewModel = viewModel(),
    navController: NavHostController
) {
    val state = vm.state
    val context = LocalContext.current

    val googleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(AuthConstants.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
    )

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            vm.onEvent(
                SignInEvent.OnGoogleSignInResult(
                    idToken = account.idToken!!,
                    onResult = { success, errorMessage ->
                        if (success) {
                            navController.navigate(NavRoutes.HOME) {
                                popUpTo(NavRoutes.SIGN_IN) { inclusive = true }
                            }
                        } else {
                            Toast.makeText(context, errorMessage, Toast.LENGTH_LONG).show()
                        }
                    }
                )
            )
        } catch (e: Exception) {
            Toast.makeText(context, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
        }
    }

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

        Spacer(modifier = Modifier.height(32.dp))

        AppTextField(
            value = state.email,
            onValueChange = { vm.onEvent(SignInEvent.OnEmailChanged(it)) },
            placeholder = "Enter your gmail",
            leadingIconRes = R.drawable.outline_email_24,
            keyboardType = KeyboardType.Email,
            isError = state.emailError != null,
            errorText = state.emailError
        )

        Spacer(modifier = Modifier.height(16.dp))

        AppPasswordField(
            value = state.password,
            onValueChange = { vm.onEvent(SignInEvent.OnPasswordChanged(it)) },
            placeholder = "Enter your Password",
            passwordVisible = state.passwordVisible,
            onVisibilityToggle = { vm.onEvent(SignInEvent.OnTogglePasswordVisibility) }
        )

        Spacer(modifier = Modifier.height(24.dp))

        AppButton(
            text = "Login",
            modifier = Modifier.fillMaxSize(0f).let { Modifier },
            onClick = {
                vm.onEvent(SignInEvent.OnLoginClick(onResult = { success ->
                    if (success) {
                        navController.navigate(NavRoutes.HOME) {
                            popUpTo(NavRoutes.SIGN_IN) { inclusive = true }
                        }
                    }
                }))
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Don't have an Account? ",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
            TextButton(
                onClick = { navController.navigate(NavRoutes.SIGN_UP) },
                contentPadding = PaddingValues(0.dp)
            ) {
                Text("Sign Up", fontSize = 15.sp, fontWeight = FontWeight.Bold, color = TealAccent)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(
            onClick = {
                val signInIntent = googleSignInClient.signInIntent
                launcher.launch(signInIntent)
            },
            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Image(
                    painter = painterResource(R.drawable.googleimage),
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Sign in using Google",
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
fun SignInScreenPreview() {
    SignInScreen(navController = NavHostController(LocalContext.current))
}