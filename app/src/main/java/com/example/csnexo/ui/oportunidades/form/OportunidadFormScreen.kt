package com.example.csnexo.ui.oportunidades.form

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Star
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
import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import com.example.csnexo.ui.components.CampoTexto
import com.example.csnexo.ui.components.MensajeError
import com.example.csnexo.ui.components.Selector
import com.example.csnexo.ui.oportunidades.etiqueta

@Composable
fun OportunidadFormRoute(
    onVolver: () -> Unit,
    onGuardado: (Long) -> Unit,
    viewModel: OportunidadFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.guardadoExitosamente) {
        if (state.guardadoExitosamente) {
            onGuardado(state.idGuardado ?: -1L)
        }
    }

    LaunchedEffect(state.eliminado) {
        if (state.eliminado) {
            onVolver()
        }
    }

    OportunidadFormScreen(
        state = state,
        onVolver = onVolver,
        onNombreChange = viewModel::onNombreChange,
        onTipoChange = viewModel::onTipoChange,
        onDescripcionChange = viewModel::onDescripcionChange,
        onFuenteChange = viewModel::onFuenteChange,
        onPrioridadChange = viewModel::onPrioridadChange,
        onPotencialChange = viewModel::onPotencialChange,
        onEstadoChange = viewModel::onEstadoChange,
        onObservacionesChange = viewModel::onObservacionesChange,
        onFotoGuardada = viewModel::onFotoGuardada,
        onFotoEliminada = viewModel::onFotoEliminada,
        onUbicacionObtenida = viewModel::onUbicacionObtenida,
        onUbicacionEliminada = viewModel::onUbicacionEliminada,
        onGuardar = viewModel::guardar,
        onEliminar = viewModel::eliminar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OportunidadFormScreen(
    state: OportunidadFormUiState,
    onVolver: () -> Unit,
    onNombreChange: (String) -> Unit,
    onTipoChange: (TipoOportunidad) -> Unit,
    onDescripcionChange: (String) -> Unit,
    onFuenteChange: (String) -> Unit,
    onPrioridadChange: (Prioridad) -> Unit,
    onPotencialChange: (PotencialComercial) -> Unit,
    onEstadoChange: (EstadoOportunidad) -> Unit,
    onObservacionesChange: (String) -> Unit,
    onFotoGuardada: (String) -> Unit,
    onFotoEliminada: () -> Unit,
    onUbicacionObtenida: (String) -> Unit,
    onUbicacionEliminada: () -> Unit,
    onGuardar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = if (state.esEdicion) {
                            "Editar oportunidad"
                        } else {
                            "Nueva oportunidad"
                        }
                    )
                },
                navigationIcon = {
                    TextButton(
                        onClick = onVolver,
                        enabled = !state.guardando
                    ) {
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
                    icono = Icons.Default.Star,
                    enabled = !state.guardando
                )

                Selector(
                    label = "Tipo *",
                    seleccionado = state.tipo,
                    opciones = TipoOportunidad.entries,
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
                    icono = Icons.Default.Description,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Fuente de información *",
                    valor = state.fuenteInformacion,
                    onCambio = onFuenteChange,
                    error = state.fuenteError,
                    icono = Icons.Default.Public,
                    enabled = !state.guardando
                )

                Selector(
                    label = "Prioridad *",
                    seleccionado = state.prioridad,
                    opciones = Prioridad.entries,
                    etiqueta = { it.etiqueta() },
                    onSeleccion = onPrioridadChange,
                    vacio = "Selecciona una prioridad",
                    error = state.prioridadError,
                    enabled = !state.guardando
                )

                Selector(
                    label = "Potencial comercial *",
                    seleccionado = state.potencial,
                    opciones = PotencialComercial.entries,
                    etiqueta = { it.etiqueta() },
                    onSeleccion = onPotencialChange,
                    vacio = "Selecciona el potencial",
                    error = state.potencialError,
                    enabled = !state.guardando
                )

                Selector(
                    label = "Estado de gestión *",
                    seleccionado = state.estado,
                    opciones = EstadoOportunidad.entries,
                    etiqueta = { it.etiqueta() },
                    onSeleccion = onEstadoChange,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Observaciones",
                    valor = state.observaciones,
                    onCambio = onObservacionesChange,
                    singleLine = false,
                    icono = Icons.Default.Info,
                    enabled = !state.guardando
                )

                EvidenciaOportunidad(
                    fotoPath = state.fotoPath,
                    ubicacionReferencia = state.ubicacionReferencia,
                    habilitado = !state.guardando,
                    onFotoGuardada = onFotoGuardada,
                    onFotoEliminada = onFotoEliminada,
                    onUbicacionObtenida = onUbicacionObtenida,
                    onUbicacionEliminada = onUbicacionEliminada
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
                            Text("Eliminar oportunidad")
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
                        Text("Guardar oportunidad")
                    }
                }
            }
        }
    }
}


