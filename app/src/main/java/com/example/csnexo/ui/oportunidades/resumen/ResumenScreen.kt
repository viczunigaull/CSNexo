package com.example.csnexo.ui.oportunidades.resumen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csnexo.di.AppViewModelProvider
import com.example.csnexo.ui.oportunidades.etiqueta

/**
 * Pantalla de resumen: muestra los datos de la oportunidad recién guardada.
 *
 * Demuestra el "pasado de información" entre pantallas: el formulario guarda y
 * navega a esta pantalla pasando el ID por la ruta; el ViewModel lo carga del
 * repositorio.
 */
@Composable
fun ResumenRoute(
    onVolver: () -> Unit,
    onEditar: (Long) -> Unit,
    viewModel: ResumenViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ResumenScreen(
        state = state,
        onVolver = onVolver,
        onEditar = onEditar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    state: ResumenUiState,
    onVolver: () -> Unit,
    onEditar: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Resumen de la oportunidad") },
                navigationIcon = { TextButton(onClick = onVolver) { Text("Lista") } }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val oportunidad = state.oportunidad
            when {
                state.cargando -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    Text(
                        text = state.error.orEmpty(),
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                oportunidad != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Card(modifier = Modifier.fillMaxWidth()) {
                            Column(
                                modifier = Modifier.padding(16.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Fila("Nombre", oportunidad.nombre)
                                Fila("Tipo", oportunidad.tipo.etiqueta())
                                Fila("Descripción", oportunidad.descripcion)
                                Fila("Fuente de información", oportunidad.fuenteInformacion)
                                Fila("Prioridad", oportunidad.prioridad.etiqueta())
                                Fila("Potencial comercial", oportunidad.potencialComercial.etiqueta())
                                Fila("Estado de gestión", oportunidad.estadoGestion.etiqueta())
                                if (oportunidad.observaciones.isNotBlank()) {
                                    Fila("Observaciones", oportunidad.observaciones)
                                }
                            }
                        }

                        Button(
                            onClick = { onEditar(oportunidad.id) },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Editar")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun Fila(etiqueta: String, valor: String) {
    Column {
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Text(
            text = valor,
            style = MaterialTheme.typography.bodyLarge
        )
    }
}
