package com.example.spendlite.AppUI

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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
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
import androidx.navigation.NavHostController
import com.example.spendlite.R
import com.example.spendlite.ui.theme.AppBackground
import com.example.spendlite.ui.theme.TealAccent
import com.example.spendlite.ui.theme.TealTint
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

@Composable
fun signUppage(
    navController: NavHostController,
) {

    var SignUpGmailID by rememberSaveable { mutableStateOf("") }
    var gmailError by rememberSaveable { mutableStateOf<String?>(null) }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
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

        Spacer(modifier = Modifier.padding(top = 32.dp))

        OutlinedTextField(
            singleLine = true,
            modifier = Modifier.fillMaxWidth(0.8f),
            value = SignUpGmailID,
            onValueChange = {
                SignUpGmailID = it
                gmailError = null
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
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            ),
            isError = gmailError != null,
            supportingText = {
                if (gmailError != null) {
                    Text(
                        text = gmailError!!,
                        color = Color.Red,
                        style = TextStyle(fontSize = 12.sp)
                    )
                }
            }
        )

        Spacer(modifier = Modifier.padding(top = 16.dp))

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
            placeholder = { Text("Create your Password", color = Color(0xFF718096)) },
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
                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(
                        painter =
                            if (passwordVisible) {
                                painterResource(R.drawable.outline_visibility_24)
                            } else {
                                painterResource(R.drawable.outline_visibility_off_24)
                            },
                        contentDescription = null,
                        tint = Color(0xFF5DCAA5)
                    )
                }
            },
            textStyle = TextStyle(
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.padding(top = 8.dp, bottom = 8.dp))
        Button(
            {
                Firebase.auth.createUserWithEmailAndPassword(SignUpGmailID, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(context, "Sign up Successful", Toast.LENGTH_SHORT).show()
                            navController.navigate("SignInPage")
                        } else {
                            gmailError = when(task.exception){
                                is FirebaseAuthInvalidCredentialsException-> "Invalid Email Format"
                                else -> task.exception?.message ?: "Signup Failed"
                            }
                        }
                    }
            },
            colors = ButtonDefaults.buttonColors(TealAccent),
            modifier = Modifier.fillMaxWidth(0.75f)
        ) {
            Text("Create Account", fontWeight = FontWeight.Bold, color = Color.Black)
        }

        TextButton(onClick = {

        }) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
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
fun signUppagePreview(modifier: Modifier = Modifier) {
    signUppage(navController = NavHostController(LocalContext.current))
}