package com.example.csnexo.ui.resultados.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.ResultadoServidor
import com.example.csnexo.domain.model.Resultado
import com.example.csnexo.domain.repository.ResultadoRepository
import com.example.csnexo.navigation.Routes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ResultadoFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ResultadoRepository,
    private val servidor: ResultadoServidor
) : ViewModel() {

    private val resultadoId: Long =
        savedStateHandle[Routes.ARG_RESULTADO_ID] ?: -1L

    private val esEdicion: Boolean = resultadoId > 0

    private val _uiState = MutableStateFlow(
        ResultadoFormUiState(
            modo = if (esEdicion) ModoResultado.EDITAR else ModoResultado.CREAR,
            cargando = esEdicion
        )
    )
    val uiState: StateFlow<ResultadoFormUiState> = _uiState.asStateFlow()

    init {
        if (esEdicion) cargar()
    }

    fun onOportunidadChange(valor: String) = actualizar { copy(oportunidad = valor, oportunidadError = null) }
    fun onResultadoChange(valor: String) = actualizar { copy(resultado = valor, resultadoError = null) }
    fun onObservacionesChange(valor: String) = actualizar { copy(observaciones = valor, observacionesError = null) }

    fun guardar() {
        val actual = _uiState.value
        if (actual.cargando || actual.guardando) return

        val conErrores = validar(actual)
        if (conErrores != null) {
            _uiState.value = conErrores
            return
        }

        val resultado = Resultado(
            id = if (esEdicion) resultadoId else 0,
            oportunidad = actual.oportunidad.trim(),
            resultado = actual.resultado.trim(),
            observaciones = actual.observaciones.trim()
        )

        _uiState.update { it.copy(guardando = true, errorGeneral = null) }

        viewModelScope.launch {
            val resultadoDb: Result<Unit> = if (esEdicion) {
                repository.actualizar(resultado)
            } else {
                repository.crear(resultado).map { Unit }
            }

            resultadoDb
                .onSuccess {
                    _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
                    viewModelScope.launch {
                        if (esEdicion) servidor.actualizar(resultado) else servidor.crear(resultado)
                    }
                }
                .onFailure {
                    _uiState.update { it.copy(guardando = false, errorGeneral = "No se pudo guardar el resultado. Intenta nuevamente.") }
                }
        }
    }

    private fun cargar() {
        viewModelScope.launch {
            val resultado = try {
                repository.obtenerPorId(resultadoId)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                null
            }

            if (resultado == null) {
                _uiState.update { it.copy(cargando = false, errorGeneral = "No se encontró el resultado.") }
                return@launch
            }
            _uiState.value = ResultadoFormUiState(
                modo = ModoResultado.EDITAR,
                cargando = false,
                oportunidad = resultado.oportunidad,
                resultado = resultado.resultado,
                observaciones = resultado.observaciones
            )
        }
    }

    private fun validar(actual: ResultadoFormUiState): ResultadoFormUiState? {
        val oportunidadError = if (actual.oportunidad.isBlank()) "La oportunidad es obligatoria." else null
        val resultadoError = if (actual.resultado.isBlank()) "El resultado es obligatorio." else null
        val observacionesError = if (actual.observaciones.isBlank()) "Las observaciones son obligatorias." else null

        val hayErrores = listOf(oportunidadError, resultadoError, observacionesError).any { it != null }
        if (!hayErrores) return null

        return actual.copy(
            oportunidadError = oportunidadError,
            resultadoError = resultadoError,
            observacionesError = observacionesError,
            errorGeneral = null
        )
    }

    private fun actualizar(bloque: ResultadoFormUiState.() -> ResultadoFormUiState) {
        _uiState.update { estado -> estado.bloque() }
    }
}
