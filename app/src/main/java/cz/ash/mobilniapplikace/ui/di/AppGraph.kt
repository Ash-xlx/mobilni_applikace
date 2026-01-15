package cz.ash.mobilniapplikace.ui.di

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import cz.ash.mobilniapplikace.data.CoinsRepository
import cz.ash.mobilniapplikace.data.auth.AuthRepository
import cz.ash.mobilniapplikace.data.auth.SupabaseClientProvider
import cz.ash.mobilniapplikace.data.local.AppDatabase
import cz.ash.mobilniapplikace.data.remote.ApiClient

@Composable
fun rememberCoinsRepository(): CoinsRepository {
    val appContext = LocalContext.current.applicationContext
    return remember(appContext) {
        CoinsRepository(
            api = ApiClient.api,
            favoritesDao = AppDatabase.get(appContext).favoritesDao()
        )
    }
}

@Composable
fun rememberAuthRepository(): AuthRepository {
    val appContext = LocalContext.current.applicationContext
    return remember(appContext) {
        AuthRepository(SupabaseClientProvider.get(appContext))
    }
}

