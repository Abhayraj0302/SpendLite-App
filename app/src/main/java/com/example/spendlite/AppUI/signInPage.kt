package com.example.spendlite.AppUI

import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.spendlite.R
import com.example.spendlite.ViewModelFiles.ViewModelApp
import com.example.spendlite.ui.theme.AppBackground
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TealTint
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import androidx.compose.ui.res.stringResource

@Composable
fun SignInPage(
    vm: ViewModelApp = viewModel(),
    navController: NavHostController
) {
    var SignInGmailID by rememberSaveable { mutableStateOf("") }
    var SignInGmailIDerror by rememberSaveable { mutableStateOf<String?>(null) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current



    val googleSignInClient = GoogleSignIn.getClient(
        context,
        GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(AuthConstants.GOOGLE_WEB_CLIENT_ID)
            .requestEmail()
            .build()
    )


    fun firebaseAuthWithGoogle(idToken: String, navController: NavHostController, context: android.content.Context) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        Firebase.auth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    navController.navigate("home") {
                        popUpTo("signIn") { inclusive = true }
                    }
                } else {
                    Toast.makeText(context, "Firebase Auth Failed: ${task.exception?.message}", Toast.LENGTH_LONG).show()
                }
            }
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
        try {
            val account = task.getResult(ApiException::class.java)
            firebaseAuthWithGoogle(account.idToken!!, navController, context)
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


        OutlinedTextField(
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            value = SignInGmailID,
            onValueChange = {
                SignInGmailID = it
                SignInGmailIDerror = null
            },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TealAccent,
                unfocusedBorderColor = TealTint,
                errorBorderColor = Color.Red
            ),
            placeholder = { Text("Enter your gmail", color = Color(0xFF718096)) },
            leadingIcon = {
                Icon(
                    painter = painterResource(R.drawable.outline_email_24),
                    contentDescription = null,
                    tint = Color(0xFF5DCAA5)
                )
            },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
            textStyle = TextStyle(color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold),
            isError = SignInGmailIDerror != null,
            supportingText = {
                if (SignInGmailIDerror != null) {
                    Text(text = SignInGmailIDerror!!, color = Color.Red, fontSize = 12.sp)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))


        OutlinedTextField(
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            value = password,
            onValueChange = { password = it },
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = TealAccent,
                unfocusedBorderColor = TealTint,
                errorBorderColor = Color.Red
            ),
            placeholder = { Text("Enter your Password", color = Color(0xFF718096)) },
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            leadingIcon = {
                Icon(painter = painterResource(R.drawable.outline_lock_24), contentDescription = null, tint = Color(0xFF5DCAA5))
            },
            trailingIcon = {
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter = if (passwordVisible) {
                            painterResource(R.drawable.outline_visibility_24)
                        } else {
                            painterResource(R.drawable.outline_visibility_off_24)
                        },
                        contentDescription = null,
                        tint = Color(0xFF5DCAA5)
                    )
                }
            },
            textStyle = TextStyle(color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Bold)
        )

        Spacer(modifier = Modifier.height(24.dp))


        Button(
            modifier = Modifier.fillMaxWidth(0.75f),
            onClick = {
                Firebase.auth.signInWithEmailAndPassword(SignInGmailID, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            navController.navigate("home") {
                                popUpTo("signIn") { inclusive = true }
                            }
                        } else {
                            val errorMessage = when (task.exception) {
                                is FirebaseAuthInvalidUserException -> "Gmail is not Registered"
                                is FirebaseAuthInvalidCredentialsException -> "Invalid Credentials"
                                else -> task.exception?.message ?: "Login Failed!!"
                            }
                            SignInGmailIDerror = errorMessage
                        }
                    }
            },
            colors = ButtonDefaults.buttonColors(TealAccent)
        ) {
            Text("Login")
        }

        Spacer(modifier = Modifier.height(16.dp))


        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                "Don't have an Account? ",
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.LightGray
            )
            TextButton(
                onClick = { navController.navigate("SignUpScreen") },
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
fun SignInPagePreview(modifier: Modifier = Modifier) {
    SignInPage(navController = NavHostController(LocalContext.current))
}