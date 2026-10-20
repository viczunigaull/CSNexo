package com.example.csnexo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.domain.repository.SesionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel raíz que decide entre login y la navegación principal.
 *
 * No es un "god ViewModel": solo traduce el flujo de sesión a un estado de UI y
 * expone la acción de cerrar sesión.
 */
class SesionViewModel(
    private val repository: SesionRepository
) : ViewModel() {

    val uiState: StateFlow<SesionUiState> = repository.sesion
        .map { usuario ->
            if (usuario == null) {
                SesionUiState.SinSesion
            } else {
                SesionUiState.ConSesion(usuario)
            }
        }
        .catch { error ->
            if (error is CancellationException) throw error
            emit(SesionUiState.SinSesion)
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.Eagerly,
            initialValue = SesionUiState.Cargando
        )

    fun cerrarSesion() {
        viewModelScope.launch {
            repository.cerrarSesion()
        }
    }
}
