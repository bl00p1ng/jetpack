package com.unilibre.taller05.presentation.inicio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.taller05.domain.model.Receta
import com.unilibre.taller05.domain.usecase.ObtenerFavoritasUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class InicioViewModel @Inject constructor(
    obtenerFavoritas: ObtenerFavoritasUseCase
) : ViewModel() {

    val favoritas: StateFlow<List<Receta>> = obtenerFavoritas()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())
}
