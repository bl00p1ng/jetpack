package com.unilibre.taller05.presentation.camara

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.unilibre.taller05.data.ml.ImageLabelerHelper
import com.unilibre.taller05.presentation.SharedSession
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CamaraViewModel @Inject constructor(
    private val labeler: ImageLabelerHelper,
    private val session: SharedSession
) : ViewModel() {

    private val _state = MutableStateFlow(CamaraState())
    val state: StateFlow<CamaraState> = _state.asStateFlow()

    fun procesar(proxy: ImageProxy, onDone: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(procesando = true, error = null)
            try {
                val ingredientes = labeler.procesarImageProxy(proxy)
                proxy.close()
                session.ingredientes.value = ingredientes.map { it.nombre }
                _state.value = _state.value.copy(procesando = false)
                onDone()
            } catch (t: Throwable) {
                proxy.close()
                _state.value = _state.value.copy(procesando = false, error = t.message)
            }
        }
    }

    fun procesarBitmap(bmp: Bitmap, rotation: Int, onDone: () -> Unit) {
        viewModelScope.launch {
            _state.value = _state.value.copy(procesando = true, error = null)
            try {
                val ingredientes = labeler.procesarBitmap(bmp, rotation)
                session.ingredientes.value = ingredientes.map { it.nombre }
                _state.value = _state.value.copy(procesando = false)
                onDone()
            } catch (t: Throwable) {
                _state.value = _state.value.copy(procesando = false, error = t.message)
            }
        }
    }
}

data class CamaraState(
    val procesando: Boolean = false,
    val error: String? = null
)
