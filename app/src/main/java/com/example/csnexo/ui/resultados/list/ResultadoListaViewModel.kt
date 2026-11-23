package com.example.csnexo.ui.resultados.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.ResultadoServidor
import com.example.csnexo.domain.repository.ResultadoRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultadoListaViewModel(
    private val repository: ResultadoRepository,
    private val servidor: ResultadoServidor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ResultadoListaUiState())
    val uiState: StateFlow<ResultadoListaUiState> = _uiState.asStateFlow()

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
                    repository.reemplazarTodos(delServidor)
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
            repository.observarTodos()
                .onStart { _uiState.update { it.copy(cargando = true, error = null) } }
                .catch { error ->
                    if (error is CancellationException) throw error
                    _uiState.update { it.copy(cargando = false, error = "No se pudieron cargar los resultados.") }
                }
                .collect { resultados ->
                    _uiState.update { it.copy(cargando = false, resultados = resultados) }
                }
        }
    }
}
