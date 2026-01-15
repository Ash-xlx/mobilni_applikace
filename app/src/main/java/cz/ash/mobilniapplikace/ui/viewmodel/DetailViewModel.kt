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

data class DetailUiState(
    val isLoading: Boolean = true,
    val errorMessage: String? = null,
    val coin: Coin? = null,
    val isFavorite: Boolean = false
)

class DetailViewModel(
    private val coinId: String,
    private val repository: CoinsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(DetailUiState(isLoading = true))
    val state: StateFlow<DetailUiState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.observeIsFavorite(coinId).collect { fav ->
                _state.update { it.copy(isFavorite = fav) }
            }
        }
        load()
    }

    fun load() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true, errorMessage = null) }
            try {
                val coin = repository.fetchCoin(coinId)
                if (coin == null) {
                    _state.update { it.copy(isLoading = false, errorMessage = "Mince nenalezena") }
                } else {
                    _state.update { it.copy(isLoading = false, coin = coin) }
                }
            } catch (t: Throwable) {
                _state.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = t.message ?: "Chyba při načítání detailu"
                    )
                }
            }
        }
    }

    fun toggleFavorite() {
        val coin = _state.value.coin ?: return
        viewModelScope.launch {
            repository.setFavorite(coin, favorite = !_state.value.isFavorite)
        }
    }
}

class DetailViewModelFactory(
    private val coinId: String,
    private val repository: CoinsRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetailViewModel(coinId, repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel: $modelClass")
    }
}

