package com.example.csnexo.ui.auth

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.domain.repository.CredencialesInvalidasException
import com.example.csnexo.domain.repository.SesionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * ViewModel exclusivo del login.
 *
 * No conoce DataStore ni el repositorio de autenticación concreto: solo usa la
 * interfaz `SesionRepository`. El estado exitoso no se guarda aquí porque el
 * ViewModel raíz observa la sesión y cambia de pantalla.
 */
class LoginViewModel(
    private val sesionRepository: SesionRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun onUsuarioChange(valor: String) {
        _uiState.update {
            it.copy(
                usuario = valor,
                usuarioError = null,
                error = null
            )
        }
    }

    fun onClaveChange(valor: String) {
        _uiState.update {
            it.copy(
                clave = valor,
                claveError = null,
                error = null
            )
        }
    }

    fun iniciarSesion() {
        val actual = _uiState.value
        if (actual.cargando) return

        val usuarioError = if (actual.usuario.isBlank()) "Ingresa tu usuario." else null
        val claveError = if (actual.clave.isBlank()) "Ingresa tu clave." else null

        if (usuarioError != null || claveError != null) {
            _uiState.update {
                it.copy(
                    usuarioError = usuarioError,
                    claveError = claveError,
                    error = null
                )
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(cargando = true, error = null) }

            sesionRepository
                .iniciarSesion(actual.usuario.trim(), actual.clave)
                .onSuccess {
                    // La sesión cambia y la raíz desmonta el login. Se limpian
                    // usuario y clave para no conservarlos en memoria.
                    _uiState.value = LoginUiState()
                }
                .onFailure { error ->
                    val mensaje = if (error is CredencialesInvalidasException) {
                        "Usuario o clave incorrectos."
                    } else {
                        "No se pudo iniciar sesión. Intenta nuevamente."
                    }
                    _uiState.update {
                        it.copy(cargando = false, error = mensaje)
                    }
                }
        }
    }
}
