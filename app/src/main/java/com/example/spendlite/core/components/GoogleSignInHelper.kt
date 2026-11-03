package com.example.spendlite.core.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.example.spendlite.core.utils.AuthConstants
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException

class GoogleSignInHandler(
    val launchGoogleSignIn: () -> Unit
)

@Composable
fun rememberGoogleSignInHandler(
    onTokenReceived: (idToken: String) -> Unit,
    onCancelled: () -> Unit
): GoogleSignInHandler {
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
            val idToken = account.idToken
            if (!idToken.isNullOrBlank()) {
                onTokenReceived(idToken)
            } else {
                onCancelled()
            }
        } catch (e: Exception) {
            onCancelled()
        }
    }

    return GoogleSignInHandler(
        launchGoogleSignIn = {
            val intent = googleSignInClient.signInIntent
            launcher.launch(intent)
        }
    )
}
