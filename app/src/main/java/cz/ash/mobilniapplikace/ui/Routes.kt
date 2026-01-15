package cz.ash.mobilniapplikace.ui

import android.net.Uri

object Routes {
    const val Dashboard = "dashboard"
    const val Explore = "explore"
    const val Profile = "profile"

    // Nav argument is string, we'll parse to Int
    const val Detail = "detail/{id}"
    fun detail(id: String) = "detail/${Uri.encode(id)}"
}

