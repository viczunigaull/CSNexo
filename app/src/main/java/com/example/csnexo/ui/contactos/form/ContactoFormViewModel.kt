package com.example.csnexo.ui.contactos.form

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.data.remote.ContactoServidor
import com.example.csnexo.domain.model.AreaContacto
import com.example.csnexo.domain.model.Contacto
import com.example.csnexo.domain.repository.ContactoRepository
import com.example.csnexo.navigation.Routes
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel del formulario de contactos.
 */
class ContactoFormViewModel(
    savedStateHandle: SavedStateHandle,
    private val repository: ContactoRepository,
    private val servidor: ContactoServidor
) : ViewModel() {

    private val contactoId: Long =
        savedStateHandle[Routes.ARG_CONTACTO_ID] ?: -1L

    private val esEdicion: Boolean = contactoId > 0

    private val _uiState = MutableStateFlow(
        ContactoFormUiState(
            modo = if (esEdicion) ModoContacto.EDITAR else ModoContacto.CREAR,
            cargando = esEdicion
        )
    )
    val uiState: StateFlow<ContactoFormUiState> = _uiState.asStateFlow()

    init {
        if (esEdicion) cargar()
    }

    fun onNombreChange(valor: String) = actualizar { copy(nombre = valor, nombreError = null) }
    fun onOrganizacionChange(valor: String) = actualizar { copy(organizacion = valor, organizacionError = null) }
    fun onCargoChange(valor: String) = actualizar { copy(cargo = valor, cargoError = null) }
    fun onAreaChange(valor: AreaContacto) = actualizar { copy(area = valor, areaError = null) }
    fun onCorreoChange(valor: String) = actualizar { copy(correo = valor, correoError = null) }
    fun onTelefonoChange(valor: String) = actualizar { copy(telefono = valor, telefonoError = null) }

    fun guardar() {
        val actual = _uiState.value
        if (actual.cargando || actual.guardando) return

        val conErrores = validar(actual)
        if (conErrores != null) {
            _uiState.value = conErrores
            return
        }

        val contacto = Contacto(
            id = if (esEdicion) contactoId else 0,
            nombre = actual.nombre.trim(),
            organizacion = actual.organizacion.trim(),
            cargo = actual.cargo.trim(),
            area = requireNotNull(actual.area),
            correo = actual.correo.trim(),
            telefono = actual.telefono.trim()
        )

        _uiState.update { it.copy(guardando = true, errorGeneral = null) }

        viewModelScope.launch {
            val resultado: Result<Unit> = if (esEdicion) {
                repository.actualizar(contacto)
            } else {
                repository.crear(contacto).map { Unit }
            }

            resultado
                .onSuccess {
                    _uiState.update { it.copy(guardando = false, guardadoExitosamente = true) }
                    viewModelScope.launch {
                        if (esEdicion) servidor.actualizar(contacto) else servidor.crear(contacto)
                    }
                }
                .onFailure {
                    _uiState.update {
                        it.copy(
                            guardando = false,
                            errorGeneral = "No se pudo guardar el contacto. Intenta nuevamente."
                        )
                    }
                }
        }
    }

    fun eliminar() {
        if (_uiState.value.eliminando) return
        _uiState.update { it.copy(eliminando = true) }
        viewModelScope.launch {
            repository.eliminar(contactoId)
            servidor.eliminar(contactoId)
            _uiState.update { it.copy(eliminando = false, eliminado = true) }
        }
    }

    private fun cargar() {
        viewModelScope.launch {
            val contacto = try {
                repository.obtenerPorId(contactoId)
            } catch (error: CancellationException) {
                throw error
            } catch (error: Exception) {
                null
            }

            if (contacto == null) {
                _uiState.update {
                    it.copy(cargando = false, errorGeneral = "No se encontró el contacto.")
                }
                return@launch
            }
            _uiState.value = ContactoFormUiState(
                modo = ModoContacto.EDITAR,
                cargando = false,
                nombre = contacto.nombre,
                organizacion = contacto.organizacion,
                cargo = contacto.cargo,
                area = contacto.area,
                correo = contacto.correo,
                telefono = contacto.telefono
            )
        }
    }

    private fun validar(actual: ContactoFormUiState): ContactoFormUiState? {
        val nombreError = if (actual.nombre.isBlank()) "El nombre es obligatorio." else null
        val organizacionError = if (actual.organizacion.isBlank()) "La organización es obligatoria." else null
        val cargoError = if (actual.cargo.isBlank()) "El cargo es obligatorio." else null
        val areaError = if (actual.area == null) "Selecciona un área." else null
        val correoError = if (actual.correo.isBlank()) "El correo es obligatorio." else null
        val telefonoError = if (actual.telefono.isBlank()) "El teléfono es obligatorio." else null

        val hayErrores = listOf(
            nombreError, organizacionError, cargoError, areaError, correoError, telefonoError
        ).any { it != null }

        if (!hayErrores) return null

        return actual.copy(
            nombreError = nombreError,
            organizacionError = organizacionError,
            cargoError = cargoError,
            areaError = areaError,
            correoError = correoError,
            telefonoError = telefonoError,
            errorGeneral = null
        )
    }

    private fun actualizar(bloque: ContactoFormUiState.() -> ContactoFormUiState) {
        _uiState.update { estado -> estado.bloque() }
    }
}
