package cz.ash.mobilniapplikace.data

import cz.ash.mobilniapplikace.data.local.FavoriteCoinEntity
import cz.ash.mobilniapplikace.data.local.FavoritesDao
import cz.ash.mobilniapplikace.data.remote.CoinGeckoApi
import cz.ash.mobilniapplikace.domain.Coin
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoinsRepository(
    private val api: CoinGeckoApi,
    private val favoritesDao: FavoritesDao
) {
    fun observeFavoriteIds(): Flow<Set<String>> =
        favoritesDao.observeAll().map { list -> list.map { it.id }.toSet() }

    fun observeFavorites(): Flow<List<Coin>> =
        favoritesDao.observeAll().map { list ->
            list.map {
                Coin(
                    id = it.id,
                    symbol = it.symbol,
                    name = it.name,
                    imageUrl = it.imageUrl,
                    priceUsd = it.lastPriceUsd,
                    change24hPct = it.lastChange24hPct,
                    marketCapUsd = it.lastMarketCapUsd
                )
            }
        }

    fun observeIsFavorite(id: String): Flow<Boolean> = favoritesDao.observeIsFavorite(id)

    suspend fun fetchTopCoins(): List<Coin> =
        api.getMarkets().map { dto ->
            Coin(
                id = dto.id,
                symbol = dto.symbol,
                name = dto.name,
                imageUrl = dto.image,
                priceUsd = dto.currentPrice,
                change24hPct = dto.priceChangePercentage24h,
                marketCapUsd = dto.marketCap
            )
        }

    suspend fun fetchCoin(id: String): Coin? {
        val list = api.getMarkets(ids = id, perPage = 1, page = 1)
        val dto = list.firstOrNull() ?: return null
        return Coin(
            id = dto.id,
            symbol = dto.symbol,
            name = dto.name,
            imageUrl = dto.image,
            priceUsd = dto.currentPrice,
            change24hPct = dto.priceChangePercentage24h,
            marketCapUsd = dto.marketCap
        )
    }

    suspend fun setFavorite(coin: Coin, favorite: Boolean) {
        if (favorite) {
            favoritesDao.upsert(
                FavoriteCoinEntity(
                    id = coin.id,
                    symbol = coin.symbol,
                    name = coin.name,
                    imageUrl = coin.imageUrl,
                    lastPriceUsd = coin.priceUsd,
                    lastChange24hPct = coin.change24hPct,
                    lastMarketCapUsd = coin.marketCapUsd
                )
            )
        } else {
            favoritesDao.deleteById(coin.id)
        }
    }

    suspend fun clearFavorites() {
        favoritesDao.clearAll()
    }
}

