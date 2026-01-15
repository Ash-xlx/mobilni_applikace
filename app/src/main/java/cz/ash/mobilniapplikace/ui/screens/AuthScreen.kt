package cz.ash.mobilniapplikace.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cz.ash.mobilniapplikace.BuildConfig
import cz.ash.mobilniapplikace.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthScreen(
    authViewModel: AuthViewModel
) {
    val state by authViewModel.state.collectAsState()
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordHidden by remember { mutableStateOf(true) }

    Scaffold(
        topBar = { TopAppBar(title = { Text("Login") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("Sign in or create account", style = MaterialTheme.typography.titleLarge)

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                label = { Text("Email") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                singleLine = true,
                modifier = Modifier.padding(top = 18.dp)
            )
            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                label = { Text("Password") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                visualTransformation = if (passwordHidden) PasswordVisualTransformation() else VisualTransformation.None,
                modifier = Modifier.padding(top = 12.dp)
            )

            Button(
                onClick = { authViewModel.signIn(email, password) },
                enabled = !state.isLoading,
                modifier = Modifier.padding(top = 18.dp)
            ) {
                Text(if (state.isLoading) "Signing in…" else "Sign in")
            }

            Button(
                onClick = { authViewModel.signUp(email, password) },
                enabled = !state.isLoading,
                modifier = Modifier.padding(top = 10.dp)
            ) {
                Text(if (state.isLoading) "Creating…" else "Create account")
            }

            if (state.error != null) {
                Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 14.dp)
                )
                Text(
                    text = "Supabase URL: ${BuildConfig.SUPABASE_URL}",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(top = 6.dp)
                )
            }
        }
    }
}

