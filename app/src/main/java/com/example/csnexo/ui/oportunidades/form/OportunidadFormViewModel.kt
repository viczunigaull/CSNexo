package com.example.csnexo.ui.oportunidades.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import com.example.csnexo.domain.repository.OportunidadRepository
import com.example.csnexo.navigation.Routes
import java.time.Instant
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel del formulario de oportunidad.
 *
 * Recibe el ID por `SavedStateHandle` cuando se edita. No conoce Room ni el DAO:
 * solo usa `OportunidadRepository`.
 */
class OportunidadFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: OportunidadRepository
) : ViewModel() {

    private val oportunidadId: Long =
        savedStateHandle[Routes.ARG_OPORTUNIDAD_ID] ?: -1L

    private val esEdicion: Boolean = oportunidadId > 0
    private var creadaEn: Instant? = null

    private val _uiState = MutableStateFlow(
        OportunidadFormUiState(
            modo = if (esEdicion) ModoFormulario.EDITAR else ModoFormulario.CREAR,
            cargando = esEdicion
        )
    )
    val uiState: StateFlow<OportunidadFormUiState> = _uiState.asStateFlow()

    init {
        if (esEdicion) {
            cargarOportunidad()
        }
    }

    fun onNombreChange(valor: String) = actualizar {
        copy(nombre = valor, nombreError = null)
    }

    fun onTipoChange(valor: TipoOportunidad) = actualizar {
        copy(tipo = valor, tipoError = null)
    }

    fun onDescripcionChange(valor: String) = actualizar {
        copy(descripcion = valor, descripcionError = null)
    }

    fun onFuenteChange(valor: String) = actualizar {
        copy(fuenteInformacion = valor, fuenteError = null)
    }

    fun onPrioridadChange(valor: Prioridad) = actualizar {
        copy(prioridad = valor, prioridadError = null)
    }

    fun onPotencialChange(valor: PotencialComercial) = actualizar {
        copy(potencial = valor, potencialError = null)
    }

    fun onEstadoChange(valor: EstadoOportunidad) = actualizar {
        copy(estado = valor)
    }

    fun onObservacionesChange(valor: String) = actualizar {
        copy(observaciones = valor)
    }

    fun onFotoGuardada(ruta: String) = actualizar {
        copy(fotoPath = ruta)
    }

    fun onFotoEliminada() = actualizar {
        copy(fotoPath = null)
    }

    fun onUbicacionObtenida(referencia: String) = actualizar {
        copy(ubicacionReferencia = referencia)
    }

    fun onUbicacionEliminada() = actualizar {
        copy(ubicacionReferencia = null)
    }

    fun guardar() {
        val actual = _uiState.value
        if (actual.cargando || actual.guardando) return

        // En edición, si no se pudo cargar la oportunidad, no se permite guardar.
        if (esEdicion && creadaEn == null) {
            _uiState.update {
                it.copy(errorGeneral = "No se encontró la oportunidad que intentas editar.")
            }
            return
        }

        // La validación ocurre antes de construir el modelo y llamar al repositorio.
        val conErrores = validar(actual)
        if (conErrores != null) {
            _uiState.value = conErrores
            return
        }

        val ahora = Instant.now()
        val oportunidad = Oportunidad(
            id = if (esEdicion) oportunidadId else 0,
            nombre = actual.nombre.trim(),
            tipo = requireNotNull(actual.tipo),
            descripcion = actual.descripcion.trim(),
            fuenteInformacion = actual.fuenteInformacion.trim(),
            prioridad = requireNotNull(actual.prioridad),
            potencialComercial = requireNotNull(actual.potencial),
            estadoGestion = actual.estado,
            observaciones = actual.observaciones.trim(),
            fotoPath = actual.fotoPath,
            ubicacionReferencia = actual.ubicacionReferencia,
            creadaEn = creadaEn ?: ahora,
            actualizadaEn = ahora
        )

        _uiState.update { it.copy(guardando = true, errorGeneral = null) }

        viewModelScope.launch {
            val resultado: Result<Unit> = if (esEdicion) {
                repository.actualizar(oportunidad)
            } else {
                repository.crear(oportunidad).map { Unit }
            }

            // El evento de navegación se deriva de guardadoExitosamente.
            resultado
                .onSuccess {
                    _uiState.update {
                        it.copy(guardando = false, guardadoExitosamente = true)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            errorGeneral = "No se pudo guardar la oportunidad. Intenta nuevamente."
                        )
                    }
                }
        }
    }

    /**
     * Carga la oportunidad cuando el formulario se abre en modo edición.
     * Si no existe, deja el formulario bloqueado para guardar.
     */
    private fun cargarOportunidad() {
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
                    it.copy(
                        cargando = false,
                        errorGeneral = "No se encontró la oportunidad que intentas editar."
                    )
                }
                return@launch
            }

            creadaEn = oportunidad.creadaEn
            _uiState.value = OportunidadFormUiState(
                modo = ModoFormulario.EDITAR,
                cargando = false,
                nombre = oportunidad.nombre,
                tipo = oportunidad.tipo,
                descripcion = oportunidad.descripcion,
                fuenteInformacion = oportunidad.fuenteInformacion,
                prioridad = oportunidad.prioridad,
                potencial = oportunidad.potencialComercial,
                estado = oportunidad.estadoGestion,
                observaciones = oportunidad.observaciones,
                fotoPath = oportunidad.fotoPath,
                ubicacionReferencia = oportunidad.ubicacionReferencia
            )
        }
    }

    /**
     * Devuelve una copia del estado con los errores encontrados, o null si el
     * formulario es válido. Así la pantalla solo muestra textos ya calculados.
     */
    private fun validar(actual: OportunidadFormUiState): OportunidadFormUiState? {
        val nombreError = when {
            actual.nombre.isBlank() -> "El nombre es obligatorio."
            actual.nombre.length > 120 -> "El nombre no puede superar los 120 caracteres."
            else -> null
        }
        val tipoError = if (actual.tipo == null) "Selecciona un tipo." else null
        val descripcionError = when {
            actual.descripcion.isBlank() -> "La descripción es obligatoria."
            actual.descripcion.length > 500 -> "La descripción no puede superar los 500 caracteres."
            else -> null
        }
        val fuenteError = when {
            actual.fuenteInformacion.isBlank() -> "La fuente de información es obligatoria."
            actual.fuenteInformacion.length > 120 -> "La fuente no puede superar los 120 caracteres."
            else -> null
        }
        val prioridadError = if (actual.prioridad == null) "Selecciona una prioridad." else null
        val potencialError = if (actual.potencial == null) "Selecciona el potencial comercial." else null

        val hayErrores = listOf(
            nombreError,
            tipoError,
            descripcionError,
            fuenteError,
            prioridadError,
            potencialError
        ).any { it != null }

        if (!hayErrores) return null

        return actual.copy(
            nombreError = nombreError,
            tipoError = tipoError,
            descripcionError = descripcionError,
            fuenteError = fuenteError,
            prioridadError = prioridadError,
            potencialError = potencialError,
            errorGeneral = null
        )
    }

    private fun actualizar(
        bloque: OportunidadFormUiState.() -> OportunidadFormUiState
    ) {
        _uiState.update { estado -> estado.bloque() }
    }
}
