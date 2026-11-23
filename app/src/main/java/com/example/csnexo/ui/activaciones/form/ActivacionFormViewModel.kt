package com.example.csnexo.ui.activaciones.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.ActivacionServidor
import com.example.csnexo.domain.model.Activacion
import com.example.csnexo.domain.repository.ActivacionRepository
import com.example.csnexo.navigation.Routes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ActivacionFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ActivacionRepository,
    private val servidor: ActivacionServidor
) : ViewModel() {

    private val activacionId: Long =
        savedStateHandle[Routes.ARG_ACTIVACION_ID] ?: -1L

    private val esEdicion: Boolean = activacionId > 0

    private val _uiState = MutableStateFlow(
        ActivacionFormUiState(
            modo = if (esEdicion) ModoActivacion.EDITAR else ModoActivacion.CREAR,
            cargando = esEdicion
        )
    )
    val uiState: StateFlow<ActivacionFormUiState> = _uiState.asStateFlow()

    init {
        if (esEdicion) cargar()
    }

    fun onOportunidadChange(valor: String) = actualizar { copy(oportunidad = valor, oportunidadError = null) }
    fun onCondicionesChange(valor: String) = actualizar { copy(condiciones = valor, condicionesError = null) }
    fun onResponsableChange(valor: String) = actualizar { copy(responsable = valor, responsableError = null) }

    fun guardar() {
        val actual = _uiState.value
        if (actual.cargando || actual.guardando) return

        val conErrores = validar(actual)
        if (conErrores != null) {
            _uiState.value = conErrores
            return
        }

        val activacion = Activacion(
            id = if (esEdicion) activacionId else 0,
            oportunidad = actual.oportunidad.trim(),
            condiciones = actual.condiciones.trim(),
            responsable = actual.responsable.trim()
        )

        _uiState.update { it.copy(guardando = true, errorGeneral = null) }

        viewModelScope.launch {
            val resultado: Result<Unit> = if (esEdicion) {
                repository.actualizar(activacion)
            } else {
                repository.crear(activacion).map { Unit }
            }

            resultado
                .onSuccess {
                    _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
                    viewModelScope.launch {
                        if (esEdicion) servidor.actualizar(activacion) else servidor.crear(activacion)
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(guardando = false, errorGeneral = "No se pudo guardar la activación. Intenta nuevamente.") }
                }
        }
    }

    private fun cargar() {
        viewModelScope.launch {
            val activacion = try {
                repository.obtenerPorId(activacionId)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                null
            }

            if (activacion == null) {
                _uiState.update { it.copy(cargando = false, errorGeneral = "No se encontró la activación.") }
                return@launch
            }
            _uiState.value = ActivacionFormUiState(
                modo = ModoActivacion.EDITAR,
                cargando = false,
                oportunidad = activacion.oportunidad,
                condiciones = activacion.condiciones,
                responsable = activacion.responsable
            )
        }
    }

    private fun validar(actual: ActivacionFormUiState): ActivacionFormUiState? {
        val oportunidadError = if (actual.oportunidad.isBlank()) "La oportunidad es obligatoria." else null
        val condicionesError = if (actual.condiciones.isBlank()) "Las condiciones son obligatorias." else null
        val responsableError = if (actual.responsable.isBlank()) "El responsable es obligatorio." else null

        val hayErrores = listOf(oportunidadError, condicionesError, responsableError).any { it != null }
        if (!hayErrores) return null

        return actual.copy(
            oportunidadError = oportunidadError,
            condicionesError = condicionesError,
            responsableError = responsableError,
            errorGeneral = null
        )
    }

    private fun actualizar(bloque: ActivacionFormUiState.() -> ActivacionFormUiState) {
        _uiState.update { estado -> estado.bloque() }
    }
}
