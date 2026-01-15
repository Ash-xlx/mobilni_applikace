package cz.ash.mobilniapplikace.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import cz.ash.mobilniapplikace.ui.viewmodel.AuthViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    authViewModel: AuthViewModel
) {
    val state by authViewModel.state.collectAsState()
    val user = state.user

    Scaffold(
        topBar = { TopAppBar(title = { Text("Profile") }) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            Text("User", style = MaterialTheme.typography.titleLarge)
            Text("ID: ${user?.id ?: "—"}", modifier = Modifier.padding(top = 10.dp))
            Text("Email: ${user?.email ?: "—"}", modifier = Modifier.padding(top = 6.dp))

            if (state.error != null) {
                Text(
                    text = state.error ?: "",
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 12.dp)
                )
            }

            OutlinedButton(
                onClick = { authViewModel.signOut() },
                enabled = !state.isLoading,
                modifier = Modifier.padding(top = 20.dp)
            ) {
                Text(if (state.isLoading) "Signing out…" else "Sign out")
            }
        }
    }
}

