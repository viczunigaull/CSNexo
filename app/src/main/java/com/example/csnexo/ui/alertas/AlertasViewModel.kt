package com.example.csnexo.ui.alertas

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.domain.repository.EventoRepository
import com.example.csnexo.domain.repository.SeguimientoRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn

/**
 * ViewModel de alertas: combina eventos y seguimientos para mostrar un resumen.
 * No crea datos nuevos, solo lee los repositorios ya existentes.
 */
class AlertasViewModel(
    eventoRepository: EventoRepository,
    seguimientoRepository: SeguimientoRepository
) : ViewModel() {

    val uiState: StateFlow<AlertasUiState> = combine(
        eventoRepository.observarTodos(),
        seguimientoRepository.observarTodos()
    ) { eventos, seguimientos ->
        AlertasUiState(
            eventosProximos = eventos.sortedBy { it.fechaInicio }.take(5),
            seguimientosRecientes = seguimientos.sortedByDescending { it.fecha }.take(5)
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = AlertasUiState()
    )
}
