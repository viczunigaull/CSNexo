package com.example.csnexo.ui.seguimientos.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.SeguimientoServidor
import com.example.csnexo.domain.model.Seguimiento
import com.example.csnexo.domain.model.TipoAccion
import com.example.csnexo.domain.repository.SeguimientoRepository
import com.example.csnexo.navigation.Routes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel del formulario de seguimientos.
 */
class SeguimientoFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: SeguimientoRepository,
    private val servidor: SeguimientoServidor
) : ViewModel() {

    private val seguimientoId: Long =
        savedStateHandle[Routes.ARG_SEGUIMIENTO_ID] ?: -1L

    private val esEdicion: Boolean = seguimientoId > 0

    private val _uiState = MutableStateFlow(
        SeguimientoFormUiState(
            modo = if (esEdicion) ModoSeguimiento.EDITAR else ModoSeguimiento.CREAR,
            cargando = esEdicion
        )
    )
    val uiState: StateFlow<SeguimientoFormUiState> = _uiState.asStateFlow()

    init {
        if (esEdicion) cargar()
    }

    fun onOportunidadChange(valor: String) = actualizar { copy(oportunidad = valor, oportunidadError = null) }
    fun onTipoAccionChange(valor: TipoAccion) = actualizar { copy(tipoAccion = valor, tipoAccionError = null) }
    fun onFechaChange(valor: String) = actualizar { copy(fecha = valor, fechaError = null) }
    fun onResultadoChange(valor: String) = actualizar { copy(resultado = valor, resultadoError = null) }

    fun guardar() {
        val actual = _uiState.value
        if (actual.cargando || actual.guardando) return

        val conErrores = validar(actual)
        if (conErrores != null) {
            _uiState.value = conErrores
            return
        }

        val seguimiento = Seguimiento(
            id = if (esEdicion) seguimientoId else 0,
            oportunidad = actual.oportunidad.trim(),
            tipoAccion = requireNotNull(actual.tipoAccion),
            fecha = actual.fecha.trim(),
            resultado = actual.resultado.trim()
        )

        _uiState.update { it.copy(guardando = true, errorGeneral = null) }

        viewModelScope.launch {
            val resultado: Result<Unit> = if (esEdicion) {
                repository.actualizar(seguimiento)
            } else {
                repository.crear(seguimiento).map { Unit }
            }

            resultado
                .onSuccess {
                    _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
                    viewModelScope.launch {
                        if (esEdicion) servidor.actualizar(seguimiento) else servidor.crear(seguimiento)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            errorGeneral = "No se pudo guardar el seguimiento. Intenta nuevamente."
                        )
                    }
                }
        }
    }

    fun eliminar() {
        if (_uiState.value.eliminando) return
        _uiState.update { it.copy(eliminando = true) }
        viewModelScope.launch {
            repository.eliminar(seguimientoId)
            servidor.eliminar(seguimientoId)
            _uiState.update { it.copy(eliminando = false, eliminado = true) }
        }
    }

    private fun cargar() {
        viewModelScope.launch {
            val seguimiento = try {
                repository.obtenerPorId(seguimientoId)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                null
            }

            if (seguimiento == null) {
                _uiState.update {
                    it.copy(cargando = false, errorGeneral = "No se encontró el seguimiento.")
                }
                return@launch
            }
            _uiState.value = SeguimientoFormUiState(
                modo = ModoSeguimiento.EDITAR,
                cargando = false,
                oportunidad = seguimiento.oportunidad,
                tipoAccion = seguimiento.tipoAccion,
                fecha = seguimiento.fecha,
                resultado = seguimiento.resultado
            )
        }
    }

    private fun validar(actual: SeguimientoFormUiState): SeguimientoFormUiState? {
        val oportunidadError = if (actual.oportunidad.isBlank()) "La oportunidad es obligatoria." else null
        val tipoAccionError = if (actual.tipoAccion == null) "Selecciona un tipo de acción." else null
        val fechaError = if (actual.fecha.isBlank()) "La fecha es obligatoria." else null
        val resultadoError = if (actual.resultado.isBlank()) "El resultado es obligatorio." else null

        val hayErrores = listOf(oportunidadError, tipoAccionError, fechaError, resultadoError)
            .any { it != null }

        if (!hayErrores) return null

        return actual.copy(
            oportunidadError = oportunidadError,
            tipoAccionError = tipoAccionError,
            fechaError = fechaError,
            resultadoError = resultadoError,
            errorGeneral = null
        )
    }

    private fun actualizar(bloque: SeguimientoFormUiState.() -> SeguimientoFormUiState) {
        _uiState.update { estado -> estado.bloque() }
    }
}
