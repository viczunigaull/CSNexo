package com.example.csnexo.ui.feriados

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.FeriadosApi
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de feriados.
 *
 * Solo depende de la interfaz [FeriadosApi], por lo que en las pruebas se puede
 * reemplazar por un fake sin tocar Retrofit.
 */
class FeriadosViewModel(
    private val api: FeriadosApi
) : ViewModel() {

    private val _uiState = MutableStateFlow(FeriadosUiState())
    val uiState: StateFlow<FeriadosUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    fun cargar(anio: Int = 2026) {
        _uiState.update { it.copy(cargando = true, error = null) }
        viewModelScope.launch {
            try {
                val feriados = api.feriados(anio)
                _uiState.value = FeriadosUiState(cargando = false, feriados = feriados)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                _uiState.update {
                    it.copy(cargando = false, error = "No se pudo cargar la información externa.")
                }
            }
        }
    }
}
