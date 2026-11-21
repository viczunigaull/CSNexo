package com.example.csnexo.ui.organizaciones.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.OrganizacionServidor
import com.example.csnexo.domain.model.Organizacion
import com.example.csnexo.domain.model.TipoOrganizacion
import com.example.csnexo.domain.repository.OrganizacionRepository
import com.example.csnexo.navigation.Routes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel del formulario de organizaciones (mismo patrón que oportunidades).
 */
class OrganizacionFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: OrganizacionRepository,
    private val servidor: OrganizacionServidor
) : ViewModel() {

    private val organizacionId: Long =
        savedStateHandle[Routes.ARG_ORGANIZACION_ID] ?: -1L

    private val esEdicion: Boolean = organizacionId > 0

    private val _uiState = MutableStateFlow(
        OrganizacionFormUiState(
            modo = if (esEdicion) ModoOrganizacion.EDITAR else ModoOrganizacion.CREAR,
            cargando = esEdicion
        )
    )
    val uiState: StateFlow<OrganizacionFormUiState> = _uiState.asStateFlow()

    init {
        if (esEdicion) cargar()
    }

    fun onNombreChange(valor: String) = actualizar { copy(nombre = valor, nombreError = null) }
    fun onTipoChange(valor: TipoOrganizacion) = actualizar { copy(tipo = valor, tipoError = null) }
    fun onRubroChange(valor: String) = actualizar { copy(rubro = valor, rubroError = null) }
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

        val organizacion = Organizacion(
            id = if (esEdicion) organizacionId else 0,
            nombre = actual.nombre.trim(),
            tipo = requireNotNull(actual.tipo),
            rubro = actual.rubro.trim(),
            comuna = actual.comuna.trim(),
            region = actual.region.trim()
        )

        _uiState.update { it.copy(guardando = true, errorGeneral = null) }

        viewModelScope.launch {
            val resultado: Result<Unit> = if (esEdicion) {
                repository.actualizar(organizacion)
            } else {
                repository.crear(organizacion).map { Unit }
            }

            resultado
                .onSuccess {
                    _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
                    // Mejor esfuerzo: también se envía al microservicio.
                    viewModelScope.launch {
                        if (esEdicion) {
                            servidor.actualizar(organizacion)
                        } else {
                            servidor.crear(organizacion)
                        }
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            errorGeneral = "No se pudo guardar la organización. Intenta nuevamente."
                        )
                    }
                }
        }
    }

    fun eliminar() {
        if (_uiState.value.eliminando) return
        _uiState.update { it.copy(eliminando = true) }
        viewModelScope.launch {
            repository.eliminar(organizacionId)
            servidor.eliminar(organizacionId)
            _uiState.update { it.copy(eliminando = false, eliminado = true) }
        }
    }

    private fun cargar() {
        viewModelScope.launch {
            val organizacion = try {
                repository.obtenerPorId(organizacionId)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                null
            }

            if (organizacion == null) {
                _uiState.update {
                    it.copy(cargando = false, errorGeneral = "No se encontró la organización.")
                }
                return@launch
            }
            _uiState.value = OrganizacionFormUiState(
                modo = ModoOrganizacion.EDITAR,
                cargando = false,
                nombre = organizacion.nombre,
                tipo = organizacion.tipo,
                rubro = organizacion.rubro,
                comuna = organizacion.comuna,
                region = organizacion.region
            )
        }
    }

    private fun validar(actual: OrganizacionFormUiState): OrganizacionFormUiState? {
        val nombreError = if (actual.nombre.isBlank()) "El nombre es obligatorio." else null
        val tipoError = if (actual.tipo == null) "Selecciona un tipo." else null
        val rubroError = if (actual.rubro.isBlank()) "El rubro es obligatorio." else null
        val comunaError = if (actual.comuna.isBlank()) "La comuna es obligatoria." else null
        val regionError = if (actual.region.isBlank()) "La región es obligatoria." else null

        val hayErrores = listOf(nombreError, tipoError, rubroError, comunaError, regionError)
            .any { it != null }

        if (!hayErrores) return null

        return actual.copy(
            nombreError = nombreError,
            tipoError = tipoError,
            rubroError = rubroError,
            comunaError = comunaError,
            regionError = regionError,
            errorGeneral = null
        )
    }

    private fun actualizar(bloque: OrganizacionFormUiState.() -> OrganizacionFormUiState) {
        _uiState.update { estado -> estado.bloque() }
    }
}
