package cz.ash.mobilniapplikace.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cz.ash.mobilniapplikace.data.CoinsRepository
import cz.ash.mobilniapplikace.domain.Coin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class HomeUiItem(
    val id: String,
    val name: String,
    val symbol: String,
    val priceUsd: Double?,
    val change24hPct: Double?,
    val isFavorite: Boolean
)

data class HomeUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val items: List<HomeUiItem> = emptyList()
)

class HomeViewModel(
    private val repository: CoinsRepository
) : ViewModel() {

    private val coins = MutableStateFlow<List<HomeUiItem>>(emptyList())
    private val _state = MutableStateFlow(HomeUiState(isLoading = true))
    val state: StateFlow<HomeUiState> = _state.asStateFlow()

    init {
        // Combine network coins with favorites from DB
        viewModelScope.launch {
            combine(coins, repository.observeFavoriteIds()) { items, favoriteIds ->
                items.map { it.copy(isFavorite = favoriteIds.contains(it.id)) }
            }.collect { combined ->
                _state.update { it.copy(items = combined) }
            }
        }

        refresh()
    }

    fun refresh() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val loaded = repository.fetchTopCoins()
                    .map { c ->
                        HomeUiItem(
                            id = c.id,
                            name = c.name,
                            symbol = c.symbol,
                            priceUsd = c.priceUsd,
                            change24hPct = c.change24hPct,
                            isFavorite = false
                        )
                    }
                coins.value = loaded
                _state.update { it.copy(isLoading = false) }
            } catch (t: Throwable) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = t.message ?: "Chyba při načítání cen"
                    )
                }
            }
        }
    }

    fun toggleFavorite(item: HomeUiItem) {
        viewModelScope.launch {
            repository.setFavorite(
                coin = Coin(
                    id = item.id,
                    symbol = item.symbol,
                    name = item.name,
                    priceUsd = item.priceUsd,
                    change24hPct = item.change24hPct,
                    marketCapUsd = null
                ),
                favorite = !item.isFavorite
            )
        }
    }
}

class HomeViewModelFactory(
    private val repository: CoinsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(HomeViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return HomeViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: $modelClass")
    }
}

