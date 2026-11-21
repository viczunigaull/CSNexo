package com.example.csnexo.ui.alertas

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csnexo.di.AppViewModelProvider
import com.example.csnexo.ui.components.Tarjeta
import com.example.csnexo.ui.eventos.etiqueta
import com.example.csnexo.ui.seguimientos.etiqueta as tipoAccionEtiqueta

/**
 * Pantalla de alertas: muestra eventos próximos y seguimientos recientes.
 * Es una vista derivada de los datos ya guardados (sin backend nuevo).
 */
@Composable
fun AlertasRoute(
    onVolver: () -> Unit,
    viewModel: AlertasViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    AlertasScreen(state = state, onVolver = onVolver)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AlertasScreen(
    state: AlertasUiState,
    onVolver: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Alertas") },
                navigationIcon = { TextButton(onClick = onVolver) { Text("Menú") } }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = "Eventos próximos",
                style = MaterialTheme.typography.titleLarge
            )
            if (state.eventosProximos.isEmpty()) {
                Text(
                    text = "No hay eventos próximos.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                state.eventosProximos.forEach { evento ->
                    Tarjeta(
                        titulo = evento.nombre,
                        lineas = listOf(
                            "${evento.fechaInicio} · ${evento.tipo.etiqueta()}",
                            "${evento.comuna}, ${evento.region}"
                        )
                    )
                }
            }

            Text(
                text = "Seguimientos recientes",
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier.padding(top = 12.dp)
            )
            if (state.seguimientosRecientes.isEmpty()) {
                Text(
                    text = "No hay seguimientos registrados.",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            } else {
                state.seguimientosRecientes.forEach { seguimiento ->
                    Tarjeta(
                        titulo = "${seguimiento.tipoAccion.tipoAccionEtiqueta()} · ${seguimiento.fecha}",
                        lineas = listOf(seguimiento.oportunidad)
                    )
                }
            }
        }
    }
}
