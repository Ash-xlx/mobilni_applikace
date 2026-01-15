package cz.ash.mobilniapplikace.data.remote

import com.squareup.moshi.Json

data class CoinMarketDto(
    val id: String,
    val symbol: String,
    val name: String,
    val image: String?,
    @Json(name = "current_price") val currentPrice: Double?,
    @Json(name = "price_change_percentage_24h") val priceChangePercentage24h: Double?,
    @Json(name = "market_cap") val marketCap: Double?
)

