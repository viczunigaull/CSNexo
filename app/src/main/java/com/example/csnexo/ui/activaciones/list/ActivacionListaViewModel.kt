package com.example.csnexo.ui.activaciones.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.ActivacionServidor
import com.example.csnexo.domain.repository.ActivacionRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivacionListaViewModel(
    private val repository: ActivacionRepository,
    private val servidor: ActivacionServidor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ActivacionListaUiState())
    val uiState: StateFlow<ActivacionListaUiState> = _uiState.asStateFlow()

    private var observacion: Job? = null

    init {
        observar()
    }

    fun reintentar() {
        observar()
    }

    fun sincronizar() {
        if (_uiState.value.sincronizando) return
        _uiState.update { it.copy(sincronizando = true, mensaje = null) }
        viewModelScope.launch {
            servidor.listar()
                .onSuccess { delServidor ->
                    repository.reemplazarTodas(delServidor)
                        .onSuccess {
                            _uiState.update { it.copy(sincronizando = false, mensaje = "Sincronizado con el servidor.") }
                        }
                        .onFailure {
                            _uiState.update { it.copy(sincronizando = false, mensaje = "No se pudo actualizar la base local.") }
                        }
                }
                .onFailure {
                    _uiState.update { it.copy(sincronizando = false, mensaje = "No se pudo conectar con el servidor.") }
                }
        }
    }

    fun eliminar(id: Long) {
        viewModelScope.launch {
            repository.eliminar(id)
            servidor.eliminar(id)
        }
    }

    private fun observar() {
        observacion?.cancel()
        observacion = viewModelScope.launch {
            repository.observarTodas()
                .onStart { _uiState.update { it.copy(cargando = true, error = null) } }
                .catch { error ->
                    if (error is CancellationException) throw error
                    _uiState.update { it.copy(cargando = false, error = "No se pudieron cargar las activaciones.") }
                }
                .collect { activaciones ->
                    _uiState.update { it.copy(cargando = false, activaciones = activaciones) }
                }
        }
    }
}
