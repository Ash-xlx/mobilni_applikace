package cz.ash.mobilniapplikace.data.auth

import android.content.Context
import cz.ash.mobilniapplikace.BuildConfig
import io.github.jan.supabase.createSupabaseClient
import io.github.jan.supabase.postgrest.Postgrest
import io.github.jan.supabase.SupabaseClient
import io.github.jan.supabase.gotrue.Auth
import io.github.jan.supabase.gotrue.MemorySessionManager

object SupabaseClientProvider {
    @Volatile private var INSTANCE: SupabaseClient? = null

    fun get(context: Context): SupabaseClient {
        return INSTANCE ?: synchronized(this) {
            INSTANCE ?: createSupabaseClient(
                supabaseUrl = BuildConfig.SUPABASE_URL,
                supabaseKey = BuildConfig.SUPABASE_ANON_KEY
            ) {
                install(Auth) {
                    // Minimal: session držíme jen v paměti (bez persistence mezi restarty).
                    // Pokud budeš chtít persistenci, přepneme na SettingsSessionManager (multiplatform-settings).
                    sessionManager = MemorySessionManager()
                }
                install(Postgrest)
            }.also { INSTANCE = it }
        }
    }
}

