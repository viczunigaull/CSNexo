package com.example.csnexo.ui.eventos.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.EventoServidor
import com.example.csnexo.domain.model.Evento
import com.example.csnexo.domain.model.TipoEvento
import com.example.csnexo.domain.repository.EventoRepository
import com.example.csnexo.navigation.Routes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel del formulario de eventos.
 */
class EventoFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: EventoRepository,
    private val servidor: EventoServidor
) : ViewModel() {

    private val eventoId: Long =
        savedStateHandle[Routes.ARG_EVENTO_ID] ?: -1L

    private val esEdicion: Boolean = eventoId > 0

    private val _uiState = MutableStateFlow(
        EventoFormUiState(
            modo = if (esEdicion) ModoEvento.EDITAR else ModoEvento.CREAR,
            cargando = esEdicion
        )
    )
    val uiState: StateFlow<EventoFormUiState> = _uiState.asStateFlow()

    init {
        if (esEdicion) cargar()
    }

    fun onNombreChange(valor: String) = actualizar { copy(nombre = valor, nombreError = null) }
    fun onTipoChange(valor: TipoEvento) = actualizar { copy(tipo = valor, tipoError = null) }
    fun onDescripcionChange(valor: String) = actualizar { copy(descripcion = valor, descripcionError = null) }
    fun onFechaInicioChange(valor: String) = actualizar { copy(fechaInicio = valor, fechaInicioError = null) }
    fun onFechaTerminoChange(valor: String) = actualizar { copy(fechaTermino = valor, fechaTerminoError = null) }
    fun onComunaChange(valor: String) = actualizar { copy(comuna = valor, comunaError = null) }
    fun onRegionChange(valor: String) = actualizar { copy(region = valor, regionError = null) }

    fun guardar() {
        val actual = _uiState.value
        if (actual.cargando || actual.guardando) return

        val conErrores = validar(actual)
        if (conErrores != null) {
            _uiState.value = conErrores
            return
        }

        val evento = Evento(
            id = if (esEdicion) eventoId else 0,
            nombre = actual.nombre.trim(),
            tipo = requireNotNull(actual.tipo),
            descripcion = actual.descripcion.trim(),
            fechaInicio = actual.fechaInicio.trim(),
            fechaTermino = actual.fechaTermino.trim(),
            comuna = actual.comuna.trim(),
            region = actual.region.trim()
        )

        _uiState.update { it.copy(guardando = true, errorGeneral = null) }

        viewModelScope.launch {
            val resultado: Result<Unit> = if (esEdicion) {
                repository.actualizar(evento)
            } else {
                repository.crear(evento).map { Unit }
            }

            resultado
                .onSuccess {
                    _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
                    viewModelScope.launch {
                        if (esEdicion) servidor.actualizar(evento) else servidor.crear(evento)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            errorGeneral = "No se pudo guardar el evento. Intenta nuevamente."
                        )
                    }
                }
        }
    }

    fun eliminar() {
        if (_uiState.value.eliminando) return
        _uiState.update { it.copy(eliminando = true) }
        viewModelScope.launch {
            repository.eliminar(eventoId)
            servidor.eliminar(eventoId)
            _uiState.update { it.copy(eliminando = false, eliminado = true) }
        }
    }

    private fun cargar() {
        viewModelScope.launch {
            val evento = try {
                repository.obtenerPorId(eventoId)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                null
            }

            if (evento == null) {
                _uiState.update {
                    it.copy(cargando = false, errorGeneral = "No se encontró el evento.")
                }
                return@launch
            }
            _uiState.value = EventoFormUiState(
                modo = ModoEvento.EDITAR,
                cargando = false,
                nombre = evento.nombre,
                tipo = evento.tipo,
                descripcion = evento.descripcion,
                fechaInicio = evento.fechaInicio,
                fechaTermino = evento.fechaTermino,
                comuna = evento.comuna,
                region = evento.region
            )
        }
    }

    private fun validar(actual: EventoFormUiState): EventoFormUiState? {
        val nombreError = if (actual.nombre.isBlank()) "El nombre es obligatorio." else null
        val tipoError = if (actual.tipo == null) "Selecciona un tipo." else null
        val descripcionError = if (actual.descripcion.isBlank()) "La descripción es obligatoria." else null
        val fechaInicioError = if (actual.fechaInicio.isBlank()) "La fecha de inicio es obligatoria." else null
        val fechaTerminoError = if (actual.fechaTermino.isBlank()) "La fecha de término es obligatoria." else null
        val comunaError = if (actual.comuna.isBlank()) "La comuna es obligatoria." else null
        val regionError = if (actual.region.isBlank()) "La región es obligatoria." else null

        val hayErrores = listOf(
            nombreError, tipoError, descripcionError, fechaInicioError,
            fechaTerminoError, comunaError, regionError
        ).any { it != null }

        if (!hayErrores) return null

        return actual.copy(
            nombreError = nombreError,
            tipoError = tipoError,
            descripcionError = descripcionError,
            fechaInicioError = fechaInicioError,
            fechaTerminoError = fechaTerminoError,
            comunaError = comunaError,
            regionError = regionError,
            errorGeneral = null
        )
    }

    private fun actualizar(bloque: EventoFormUiState.() -> EventoFormUiState) {
        _uiState.update { estado -> estado.bloque() }
    }
}
