package cz.ash.mobilniapplikace.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import cz.ash.mobilniapplikace.data.CoinsRepository
import cz.ash.mobilniapplikace.domain.Coin
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class FavoritesUiState(
    val items: List<Coin> = emptyList()
)

class FavoritesViewModel(
    private val repository: CoinsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(FavoritesUiState())
    val state: StateFlow<FavoritesUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeFavorites().collect { favorites ->
                _state.update { it.copy(items = favorites) }
            }
        }
    }

    fun removeFromFavorites(coin: Coin) {
        viewModelScope.launch {
            repository.setFavorite(coin, favorite = false)
        }
    }
}

class FavoritesViewModelFactory(
    private val repository: CoinsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(FavoritesViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return FavoritesViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: $modelClass")
    }
}

