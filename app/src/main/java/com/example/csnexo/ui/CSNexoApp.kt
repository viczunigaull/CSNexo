package com.example.csnexo.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csnexo.di.AppViewModelProvider
import com.example.csnexo.navigation.CSNexoNavHost
import com.example.csnexo.ui.auth.LoginRoute
import com.example.csnexo.ui.auth.SesionUiState
import com.example.csnexo.ui.auth.SesionViewModel

/**
 * Raíz de la aplicación: decide si mostrar login o la navegación principal.
 *
 * El login queda fuera del NavHost para que cerrar sesión desmonte el grafo
 * autenticado y no se pueda volver con el botón "atrás".
 */
@Composable
fun CSNexoApp(
    sesionViewModel: SesionViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val sesionState by sesionViewModel.uiState.collectAsStateWithLifecycle()

    when (val estado = sesionState) {
        SesionUiState.Cargando -> PantallaCargando()

        SesionUiState.SinSesion -> LoginRoute()

        is SesionUiState.ConSesion -> CSNexoNavHost(
            usuario = estado.usuario,
            onCerrarSesion = sesionViewModel::cerrarSesion
        )
    }
}

@Composable
private fun PantallaCargando() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}
