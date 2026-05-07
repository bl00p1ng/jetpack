package com.andresillo.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresillo.newsapp.model.News
import com.andresillo.newsapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

sealed class ListUiState {
    object Loading : ListUiState()
    data class Success(val news: List<News>) : ListUiState()
    data class Error(val message: String) : ListUiState()
}

@HiltViewModel
class ListScreenViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow<ListUiState>(ListUiState.Loading)
    val uiState: StateFlow<ListUiState> = _uiState.asStateFlow()

    init {
        loadNews()
    }

    fun loadNews(country: String = "us") {
        viewModelScope.launch {
            _uiState.value = ListUiState.Loading
            try {
                val news = repository.getNews(country)
                _uiState.value = ListUiState.Success(news)
            } catch (e: Exception) {
                _uiState.value = ListUiState.Error(e.message ?: "Error desconocido")
            }
        }
    }
}
