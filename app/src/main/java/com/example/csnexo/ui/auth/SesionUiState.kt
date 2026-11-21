package com.example.csnexo.ui.auth

import com.example.csnexo.domain.model.Usuario

/**
 * Estado de sesión de la aplicación.
 *
 * Es un estado mutuamente excluyente: mientras se lee DataStore no se puede
 * saber todavía si hay sesión, y una vez resuelto solo existe uno de los dos
 * casos posibles.
 */
sealed interface SesionUiState {

    data object Cargando : SesionUiState

    data object SinSesion : SesionUiState

    data class ConSesion(val usuario: Usuario) : SesionUiState
}
