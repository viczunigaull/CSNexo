package com.example.csnexo.ui.oportunidades.list

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csnexo.di.AppViewModelProvider
import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import com.example.csnexo.ui.components.Selector
import com.example.csnexo.ui.components.Tarjeta
import com.example.csnexo.ui.oportunidades.etiqueta
import com.example.csnexo.ui.theme.CSNexoTheme
import java.time.Instant

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaOportunidadesRoute(
    onVolver: () -> Unit,
    onAgregar: () -> Unit,
    onEditar: (Long) -> Unit,
    viewModel: ListaOportunidadesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    ListaOportunidadesScreen(
        state = state,
        onVolver = onVolver,
        onAgregar = onAgregar,
        onEditar = onEditar,
        onReintentar = viewModel::reintentar,
        onSincronizar = viewModel::sincronizar,
        onFiltroEstadoChange = viewModel::onFiltroEstadoChange
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListaOportunidadesScreen(
    state: ListaOportunidadesUiState,
    onVolver: () -> Unit,
    onAgregar: () -> Unit,
    onEditar: (Long) -> Unit,
    onReintentar: () -> Unit,
    onSincronizar: () -> Unit,
    onFiltroEstadoChange: (EstadoOportunidad?) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Oportunidades") },
                navigationIcon = {
                    TextButton(onClick = onVolver) {
                        Text("Menú")
                    }
                },
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
            FloatingActionButton(
                onClick = onAgregar,
                modifier = Modifier.semantics {
                    contentDescription = "Nueva oportunidad"
                }
            ) {
                Text(
                    text = "+",
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when {
                state.cargando && state.oportunidades.isEmpty() -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }

                state.error != null -> {
                    EstadoError(
                        mensaje = state.error,
                        onReintentar = onReintentar
                    )
                }

                state.oportunidades.isEmpty() -> {
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
                        item(key = "filtro") {
                            Column {
                                Selector(
                                    label = "Filtrar por estado",
                                    seleccionado = state.filtroEstado,
                                    opciones = EstadoOportunidad.entries,
                                    etiqueta = { it.etiqueta() },
                                    onSeleccion = onFiltroEstadoChange,
                                    vacio = "Todos los estados"
                                )
                                if (state.filtroEstado != null) {
                                    TextButton(onClick = { onFiltroEstadoChange(null) }) {
                                        Text("Limpiar filtro")
                                    }
                                }
                            }
                        }
                        if (state.mensaje != null) {
                            item(key = "mensaje") {
                                Text(
                                    text = state.mensaje.orEmpty(),
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        items(
                            items = state.oportunidades,
                            key = { oportunidad -> oportunidad.id }
                        ) { oportunidad ->
                            Tarjeta(
                                titulo = oportunidad.nombre,
                                lineas = listOf(
                                    "${oportunidad.tipo.etiqueta()} · Prioridad ${oportunidad.prioridad.etiqueta()}",
                                    "Estado: ${oportunidad.estadoGestion.etiqueta()}"
                                ),
                                onClick = { onEditar(oportunidad.id) }
                            )
                        }
                    }
                }
            }
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
        Text(
            text = "Todavía no hay oportunidades",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = "Registra la primera oportunidad para comenzar el seguimiento.",
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        OutlinedButton(
            onClick = onAgregar,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Crear oportunidad")
        }
    }
}

@Composable
private fun EstadoError(
    mensaje: String,
    onReintentar: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "No se pudo cargar la lista",
            style = MaterialTheme.typography.titleMedium
        )
        Text(
            text = mensaje,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )
        Button(
            onClick = onReintentar,
            modifier = Modifier.padding(top = 20.dp)
        ) {
            Text("Reintentar")
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ListaOportunidadesPreview() {
    val ahora = Instant.now()
    CSNexoTheme {
        ListaOportunidadesScreen(
            state = ListaOportunidadesUiState(
                cargando = false,
                oportunidades = listOf(
                    Oportunidad(
                        id = 1,
                        nombre = "Patrocinio Feria Empresarial",
                        tipo = TipoOportunidad.PATROCINIO,
                        descripcion = "Evaluar presencia de marca en el escenario principal.",
                        fuenteInformacion = "Fuente ficticia",
                        prioridad = Prioridad.ALTA,
                        potencialComercial = PotencialComercial.ALTO,
                        estadoGestion = EstadoOportunidad.EVENTO_IDENTIFICADO,
                        creadaEn = ahora,
                        actualizadaEn = ahora
                    )
                )
            ),
            onVolver = {},
            onAgregar = {},
            onEditar = {},
            onReintentar = {},
            onSincronizar = {},
            onFiltroEstadoChange = {}
        )
    }
}
