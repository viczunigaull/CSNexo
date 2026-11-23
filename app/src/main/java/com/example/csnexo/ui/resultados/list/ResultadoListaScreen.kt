package com.example.csnexo.ui.resultados.list

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csnexo.di.AppViewModelProvider
import com.example.csnexo.domain.model.Resultado
import com.example.csnexo.ui.components.ConfirmarEliminacion
import com.example.csnexo.ui.components.Tarjeta

@Composable
fun ResultadoListaRoute(
    onVolver: () -> Unit,
    onAgregar: () -> Unit,
    onEditar: (Long) -> Unit,
    viewModel: ResultadoListaViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ResultadoListaScreen(
        state = state,
        onVolver = onVolver,
        onAgregar = onAgregar,
        onEditar = onEditar,
        onReintentar = viewModel::reintentar,
        onSincronizar = viewModel::sincronizar,
        onEliminar = viewModel::eliminar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResultadoListaScreen(
    state: ResultadoListaUiState,
    onVolver: () -> Unit,
    onAgregar: () -> Unit,
    onEditar: (Long) -> Unit,
    onReintentar: () -> Unit,
    onSincronizar: () -> Unit,
    onEliminar: (Long) -> Unit,
    modifier: Modifier = Modifier
) {
    var resultadoAEliminar by remember { mutableStateOf<Resultado?>(null) }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Resultados") },
                navigationIcon = { TextButton(onClick = onVolver) { Text("Menú") } },
                actions = {
                    TextButton(onClick = onSincronizar, enabled = !state.sincronizando) {
                        if (state.sincronizando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(18.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Sincronizar")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onAgregar) {
                Text(text = "+", style = MaterialTheme.typography.headlineMedium)
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.cargando && state.resultados.isEmpty() -> {
                    CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
                }

                state.error != null -> {
                    EstadoError(mensaje = state.error.orEmpty(), onReintentar = onReintentar)
                }

                state.resultados.isEmpty() -> {
                    EstadoVacio(onAgregar = onAgregar)
                }

                else -> {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(
                            start = 16.dp,
                            top = 16.dp,
                            end = 16.dp,
                            bottom = 96.dp
                        ),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        if (state.mensaje != null) {
                            item(key = "mensaje") {
                                Text(
                                    text = state.mensaje.orEmpty(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        items(items = state.resultados, key = { it.id }) { resultado ->
                            Tarjeta(
                                titulo = resultado.oportunidad,
                                lineas = listOf(
                                    "Resultado: ${resultado.resultado}",
                                    "Observaciones: ${resultado.observaciones}"
                                ),
                                onClick = { onEditar(resultado.id) },
                                onEliminar = { resultadoAEliminar = resultado }
                            )
                        }
                    }
                }
            }
        }

        val aEliminar = resultadoAEliminar
        if (aEliminar != null) {
            ConfirmarEliminacion(
                titulo = "Eliminar resultado",
                mensaje = "¿Seguro que quieres eliminar el resultado de \"${aEliminar.oportunidad}\"? Esta acción no se puede deshacer.",
                onConfirmar = {
                    onEliminar(aEliminar.id)
                    resultadoAEliminar = null
                },
                onCancelar = { resultadoAEliminar = null }
            )
        }
    }
}

@Composable
private fun EstadoVacio(onAgregar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Todavía no hay resultados", style = MaterialTheme.typography.titleMedium)
        Text(
            text = "Registra el primer resultado.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedButton(onClick = onAgregar, modifier = Modifier.padding(top = 20.dp)) {
            Text("Crear resultado")
        }
    }
}

@Composable
private fun EstadoError(mensaje: String, onReintentar: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("No se pudo cargar la lista", style = MaterialTheme.typography.titleMedium)
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(onClick = onReintentar, modifier = Modifier.padding(top = 20.dp)) {
            Text("Reintentar")
        }
    }
}
