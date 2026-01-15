package cz.ash.mobilniapplikace.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite_coins")
data class FavoriteCoinEntity(
    @PrimaryKey val id: String,
    val symbol: String,
    val name: String,
    val lastPriceUsd: Double?,
    val lastChange24hPct: Double?,
    val lastMarketCapUsd: Double?
)

