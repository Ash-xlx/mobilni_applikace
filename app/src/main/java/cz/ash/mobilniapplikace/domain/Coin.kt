package cz.ash.mobilniapplikace.domain

data class Coin(
    val id: String,
    val symbol: String,
    val name: String,
    val imageUrl: String?,
    val priceUsd: Double?,
    val change24hPct: Double?,
    val marketCapUsd: Double?
)

