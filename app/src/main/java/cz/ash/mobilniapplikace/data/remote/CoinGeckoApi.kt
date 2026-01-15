package cz.ash.mobilniapplikace.data.remote

import retrofit2.http.GET
import retrofit2.http.Query

/**
 * CoinGecko API v3 (bez API klíče).
 * Docs: https://www.coingecko.com/en/api/documentation
 */
interface CoinGeckoApi {
    @GET("api/v3/coins/markets")
    suspend fun getMarkets(
        @Query("vs_currency") vsCurrency: String = "usd",
        @Query("order") order: String = "market_cap_desc",
        @Query("per_page") perPage: Int = 50,
        @Query("page") page: Int = 1,
        @Query("sparkline") sparkline: Boolean = false,
        @Query("price_change_percentage") priceChangePercentage: String = "24h",
        @Query("ids") ids: String? = null
    ): List<CoinMarketDto>
}

