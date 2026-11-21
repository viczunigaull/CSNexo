package com.example.csnexo.ui.seguimientos.form

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
import com.example.csnexo.domain.model.TipoAccion
import com.example.csnexo.ui.components.CampoTexto
import com.example.csnexo.ui.components.MensajeError
import com.example.csnexo.ui.components.Selector
import com.example.csnexo.ui.seguimientos.etiqueta

@Composable
fun SeguimientoFormRoute(
    onVolver: () -> Unit,
    onGuardado: () -> Unit,
    viewModel: SeguimientoFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
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

    SeguimientoFormScreen(
        state = state,
        onVolver = onVolver,
        onOportunidadChange = viewModel::onOportunidadChange,
        onTipoAccionChange = viewModel::onTipoAccionChange,
        onFechaChange = viewModel::onFechaChange,
        onResultadoChange = viewModel::onResultadoChange,
        onGuardar = viewModel::guardar,
        onEliminar = viewModel::eliminar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SeguimientoFormScreen(
    state: SeguimientoFormUiState,
    onVolver: () -> Unit,
    onOportunidadChange: (String) -> Unit,
    onTipoAccionChange: (TipoAccion) -> Unit,
    onFechaChange: (String) -> Unit,
    onResultadoChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (state.esEdicion) "Editar seguimiento" else "Nuevo seguimiento")
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
                    label = "Oportunidad *",
                    valor = state.oportunidad,
                    onCambio = onOportunidadChange,
                    error = state.oportunidadError,
                    enabled = !state.guardando
                )

                Selector(
                    label = "Tipo de acción *",
                    seleccionado = state.tipoAccion,
                    opciones = TipoAccion.entries,
                    etiqueta = { it.etiqueta() },
                    onSeleccion = onTipoAccionChange,
                    vacio = "Selecciona un tipo",
                    error = state.tipoAccionError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Fecha * (yyyy-MM-dd)",
                    valor = state.fecha,
                    onCambio = onFechaChange,
                    error = state.fechaError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Resultado *",
                    valor = state.resultado,
                    onCambio = onResultadoChange,
                    singleLine = false,
                    error = state.resultadoError,
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
                            Text("Eliminar seguimiento")
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
                        Text("Guardar seguimiento")
                    }
                }
            }
        }
    }
}
