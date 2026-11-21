package com.example.csnexo.ui.oportunidades.resumen

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.domain.repository.OportunidadRepository
import com.example.csnexo.navigation.Routes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel de la pantalla de resumen.
 *
 * Recibe el ID de la oportunidad por `SavedStateHandle` (la ruta lleva el ID
 * como argumento) y la carga desde el repositorio para mostrarla.
 */
class ResumenViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: OportunidadRepository
) : ViewModel() {

    private val oportunidadId: Long =
        savedStateHandle[Routes.ARG_OPORTUNIDAD_ID] ?: -1L

    private val _uiState = MutableStateFlow(ResumenUiState())
    val uiState: StateFlow<ResumenUiState> = _uiState.asStateFlow()

    init {
        cargar()
    }

    private fun cargar() {
        viewModelScope.launch {
            val oportunidad = try {
                repository.obtenerPorId(oportunidadId)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                null
            }

            if (oportunidad == null) {
                _uiState.update {
                    it.copy(cargando = false, error = "No se encontró la oportunidad.")
                }
            } else {
                _uiState.update {
                    it.copy(cargando = false, oportunidad = oportunidad)
                }
            }
        }
    }
}
