package com.example.csnexo.ui.eventos.form

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csnexo.di.AppViewModelProvider
import com.example.csnexo.domain.model.TipoEvento
import com.example.csnexo.ui.components.CampoTexto
import com.example.csnexo.ui.components.MensajeError
import com.example.csnexo.ui.components.Selector
import com.example.csnexo.ui.eventos.etiqueta

@Composable
fun EventoFormRoute(
    onVolver: () -> Unit,
    onGuardado: () -> Unit,
    viewModel: EventoFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.guardadoExitosamente) {
        if (state.guardadoExitosamente) {
            onGuardado()
        }
    }

    LaunchedEffect(state.eliminado) {
        if (state.eliminado) {
            onVolver()
        }
    }

    EventoFormScreen(
        state = state,
        onVolver = onVolver,
        onNombreChange = viewModel::onNombreChange,
        onTipoChange = viewModel::onTipoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onFechaInicioChange = viewModel::onFechaInicioChange,
        onFechaTerminoChange = viewModel::onFechaTerminoChange,
        onComunaChange = viewModel::onComunaChange,
        onRegionChange = viewModel::onRegionChange,
        onGuardar = viewModel::guardar,
        onEliminar = viewModel::eliminar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventoFormScreen(
    state: EventoFormUiState,
    onVolver: () -> Unit,
    onNombreChange: (String) -> Unit,
    onTipoChange: (TipoEvento) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onFechaInicioChange: (String) -> Unit,
    onFechaTerminoChange: (String) -> Unit,
    onComunaChange: (String) -> Unit,
    onRegionChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (state.esEdicion) "Editar evento" else "Nuevo evento")
                },
                navigationIcon = {
                    TextButton(onClick = onVolver, enabled = !state.guardando) {
                        Text("Volver")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (state.cargando) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 20.dp, vertical = 16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                MensajeError(state.errorGeneral)

                CampoTexto(
                    label = "Nombre *",
                    valor = state.nombre,
                    onCambio = onNombreChange,
                    error = state.nombreError,
                    enabled = !state.guardando
                )

                Selector(
                    label = "Tipo *",
                    seleccionado = state.tipo,
                    opciones = TipoEvento.entries,
                    etiqueta = { it.etiqueta() },
                    onSeleccion = onTipoChange,
                    vacio = "Selecciona un tipo",
                    error = state.tipoError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Descripción *",
                    valor = state.descripcion,
                    onCambio = onDescripcionChange,
                    singleLine = false,
                    error = state.descripcionError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Fecha de inicio * (yyyy-MM-dd)",
                    valor = state.fechaInicio,
                    onCambio = onFechaInicioChange,
                    error = state.fechaInicioError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Fecha de término * (yyyy-MM-dd)",
                    valor = state.fechaTermino,
                    onCambio = onFechaTerminoChange,
                    error = state.fechaTerminoError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Comuna *",
                    valor = state.comuna,
                    onCambio = onComunaChange,
                    error = state.comunaError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Región *",
                    valor = state.region,
                    onCambio = onRegionChange,
                    error = state.regionError,
                    enabled = !state.guardando
                )

                Spacer(modifier = Modifier.height(8.dp))

                if (state.esEdicion) {
                    OutlinedButton(
                        onClick = onEliminar,
                        enabled = !state.guardando && !state.eliminando,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        if (state.eliminando) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(22.dp),
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text("Eliminar evento")
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Button(
                    onClick = onGuardar,
                    enabled = !state.guardando,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    if (state.guardando) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(22.dp),
                            strokeWidth = 2.dp,
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text("Guardar evento")
                    }
                }
            }
        }
    }
}
