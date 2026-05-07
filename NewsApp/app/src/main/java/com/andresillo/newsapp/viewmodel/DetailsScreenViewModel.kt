package com.andresillo.newsapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.andresillo.newsapp.model.News
import com.andresillo.newsapp.repository.NewsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsScreenViewModel @Inject constructor(
    private val repository: NewsRepository
) : ViewModel() {

    private val _news = MutableStateFlow<News?>(null)
    val news: StateFlow<News?> = _news.asStateFlow()

    fun loadNews(title: String) {
        viewModelScope.launch(Dispatchers.IO) {
            _news.value = repository.getNew(title)
        }
    }
}
