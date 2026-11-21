package com.example.csnexo.ui.auth

/**
 * Estado de la pantalla de login.
 *
 * Los errores son textos listos para mostrar en `supportingText`; por eso viven
 * en el UiState y no en el composable.
 */
data class LoginUiState(
    val usuario: String = "",
    val clave: String = "",
    val cargando: Boolean = false,
    val usuarioError: String? = null,
    val claveError: String? = null,
    val error: String? = null
)
