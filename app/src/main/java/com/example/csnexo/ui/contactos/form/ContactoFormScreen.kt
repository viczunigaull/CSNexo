package com.example.csnexo.ui.contactos.form

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
import com.example.csnexo.domain.model.AreaContacto
import com.example.csnexo.ui.components.CampoTexto
import com.example.csnexo.ui.components.MensajeError
import com.example.csnexo.ui.components.Selector
import com.example.csnexo.ui.contactos.etiqueta

@Composable
fun ContactoFormRoute(
    onVolver: () -> Unit,
    onGuardado: () -> Unit,
    viewModel: ContactoFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
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

    ContactoFormScreen(
        state = state,
        onVolver = onVolver,
        onNombreChange = viewModel::onNombreChange,
        onOrganizacionChange = viewModel::onOrganizacionChange,
        onCargoChange = viewModel::onCargoChange,
        onAreaChange = viewModel::onAreaChange,
        onCorreoChange = viewModel::onCorreoChange,
        onTelefonoChange = viewModel::onTelefonoChange,
        onGuardar = viewModel::guardar,
        onEliminar = viewModel::eliminar
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContactoFormScreen(
    state: ContactoFormUiState,
    onVolver: () -> Unit,
    onNombreChange: (String) -> Unit,
    onOrganizacionChange: (String) -> Unit,
    onCargoChange: (String) -> Unit,
    onAreaChange: (AreaContacto) -> Unit,
    onCorreoChange: (String) -> Unit,
    onTelefonoChange: (String) -> Unit,
    onGuardar: () -> Unit,
    onEliminar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Text(text = if (state.esEdicion) "Editar contacto" else "Nuevo contacto")
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

                CampoTexto(
                    label = "Organización *",
                    valor = state.organizacion,
                    onCambio = onOrganizacionChange,
                    error = state.organizacionError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Cargo *",
                    valor = state.cargo,
                    onCambio = onCargoChange,
                    error = state.cargoError,
                    enabled = !state.guardando
                )

                Selector(
                    label = "Área *",
                    seleccionado = state.area,
                    opciones = AreaContacto.entries,
                    etiqueta = { it.etiqueta() },
                    onSeleccion = onAreaChange,
                    vacio = "Selecciona un área",
                    error = state.areaError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Correo *",
                    valor = state.correo,
                    onCambio = onCorreoChange,
                    error = state.correoError,
                    enabled = !state.guardando
                )

                CampoTexto(
                    label = "Teléfono *",
                    valor = state.telefono,
                    onCambio = onTelefonoChange,
                    error = state.telefonoError,
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
                            Text("Eliminar contacto")
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
                        Text("Guardar contacto")
                    }
                }
            }
        }
    }
}
