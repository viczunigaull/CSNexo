package com.example.csnexo.ui.oportunidades.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.OportunidadServidor
import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.repository.OportunidadRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel de la lista de oportunidades.
 *
 * Observa la base local (Room) para mostrar la lista y, además, puede
 * sincronizar con el microservicio Spring Boot a través de [OportunidadServidor].
 */
class ListaOportunidadesViewModel(
    private val repository: OportunidadRepository,
    private val servidor: OportunidadServidor
) : ViewModel() {

    private val _uiState = MutableStateFlow(ListaOportunidadesUiState())
    val uiState: StateFlow<ListaOportunidadesUiState> = _uiState.asStateFlow()

    private var observacion: Job? = null
    private var todas: List<Oportunidad> = emptyList()

    init {
        observar()
    }

    fun reintentar() {
        observar()
    }

    /** Filtra la lista por estado; null muestra todas. */
    fun onFiltroEstadoChange(estado: EstadoOportunidad?) {
        _uiState.update {
            it.copy(filtroEstado = estado, oportunidades = filtrar(todas, estado))
        }
    }

    private fun filtrar(
        lista: List<Oportunidad>,
        filtro: EstadoOportunidad?
    ): List<Oportunidad> =
        if (filtro == null) lista else lista.filter { it.estadoGestion == filtro }

    /**
     * Trae las oportunidades desde el microservicio y reemplaza la lista local.
     * Si el servidor no está disponible, la app sigue funcionando con Room.
     */
    fun sincronizar() {
        if (_uiState.value.sincronizando) return
        _uiState.update { it.copy(sincronizando = true, mensaje = null) }
        viewModelScope.launch {
            servidor.listar()
                .onSuccess { delServidor ->
                    repository.reemplazarTodas(delServidor)
                        .onSuccess {
                            _uiState.update {
                                it.copy(
                                    sincronizando = false,
                                    mensaje = "Sincronizado con el servidor."
                                )
                            }
                        }
                        .onFailure {
                            _uiState.update {
                                it.copy(
                                    sincronizando = false,
                                    mensaje = "No se pudo actualizar la base local."
                                )
                            }
                        }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            sincronizando = false,
                            mensaje = "No se pudo conectar con el servidor."
                        )
                    }
                }
        }
    }

    /** Elimina la oportunidad en local y en el servidor. */
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
                .onStart {
                    _uiState.update { it.copy(cargando = true, error = null) }
                }
                .catch { error ->
                    if (error is CancellationException) throw error
                    _uiState.update {
                        it.copy(
                            cargando = false,
                            error = "No se pudieron cargar las oportunidades."
                        )
                    }
                }
                .collect { oportunidades ->
                    todas = oportunidades
                    _uiState.update {
                        it.copy(cargando = false, oportunidades = filtrar(todas, it.filtroEstado))
                    }
                }
        }
    }
}
