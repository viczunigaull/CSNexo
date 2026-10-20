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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csnexo.di.AppViewModelProvider
import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import com.example.csnexo.ui.oportunidades.etiqueta
import com.example.csnexo.ui.theme.CSNexoTheme

@Composable
fun OportunidadFormRoute(
    onVolver: () -> Unit,
    onGuardado: () -> Unit,
    viewModel: OportunidadFormViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(state.guardadoExitosamente) {
        if (state.guardadoExitosamente) {
            onGuardado()
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
        onGuardar = viewModel::guardar
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
    onGuardar: () -> Unit,
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
                if (state.errorGeneral != null) {
                    Card(
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = state.errorGeneral,
                            color = MaterialTheme.colorScheme.onErrorContainer,
                            style = MaterialTheme.typography.bodyMedium,
                            modifier = Modifier.padding(16.dp)
                        )
                    }
                }

                OutlinedTextField(
                    value = state.nombre,
                    onValueChange = onNombreChange,
                    label = { Text("Nombre *") },
                    singleLine = true,
                    enabled = !state.guardando,
                    isError = state.nombreError != null,
                    supportingText = if (state.nombreError != null) {
                        { Text(state.nombreError.orEmpty()) }
                    } else {
                        null
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                SelectorCampo(
                    label = "Tipo *",
                    seleccionado = state.tipo?.etiqueta(),
                    opciones = TipoOportunidad.entries.map { it to it.etiqueta() },
                    onSeleccionar = onTipoChange,
                    error = state.tipoError,
                    habilitado = !state.guardando
                )

                OutlinedTextField(
                    value = state.descripcion,
                    onValueChange = onDescripcionChange,
                    label = { Text("Descripción *") },
                    minLines = 3,
                    maxLines = 6,
                    enabled = !state.guardando,
                    isError = state.descripcionError != null,
                    supportingText = if (state.descripcionError != null) {
                        { Text(state.descripcionError.orEmpty()) }
                    } else {
                        null
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = state.fuenteInformacion,
                    onValueChange = onFuenteChange,
                    label = { Text("Fuente de información *") },
                    singleLine = true,
                    enabled = !state.guardando,
                    isError = state.fuenteError != null,
                    supportingText = if (state.fuenteError != null) {
                        { Text(state.fuenteError.orEmpty()) }
                    } else {
                        null
                    },
                    modifier = Modifier.fillMaxWidth()
                )

                SelectorCampo(
                    label = "Prioridad *",
                    seleccionado = state.prioridad?.etiqueta(),
                    opciones = Prioridad.entries.map { it to it.etiqueta() },
                    onSeleccionar = onPrioridadChange,
                    error = state.prioridadError,
                    habilitado = !state.guardando
                )

                SelectorCampo(
                    label = "Potencial comercial *",
                    seleccionado = state.potencial?.etiqueta(),
                    opciones = PotencialComercial.entries.map { it to it.etiqueta() },
                    onSeleccionar = onPotencialChange,
                    error = state.potencialError,
                    habilitado = !state.guardando
                )

                SelectorCampo(
                    label = "Estado de gestión *",
                    seleccionado = state.estado.etiqueta(),
                    opciones = EstadoOportunidad.entries.map { it to it.etiqueta() },
                    onSeleccionar = onEstadoChange,
                    error = null,
                    habilitado = !state.guardando
                )

                OutlinedTextField(
                    value = state.observaciones,
                    onValueChange = onObservacionesChange,
                    label = { Text("Observaciones") },
                    minLines = 2,
                    maxLines = 5,
                    enabled = !state.guardando,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(8.dp))

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

@Composable
private fun <T> SelectorCampo(
    label: String,
    seleccionado: String?,
    opciones: List<Pair<T, String>>,
    onSeleccionar: (T) -> Unit,
    error: String?,
    habilitado: Boolean
) {
    var expandido by remember { mutableStateOf(false) }

    Column {
        Text(
            text = label,
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Box {
            OutlinedButton(
                onClick = { expandido = true },
                enabled = habilitado,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(
                    text = seleccionado ?: "Selecciona una opción",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Start,
                    color = if (seleccionado == null) {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    }
                )
                Text(text = "▾")
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                opciones.forEach { (valor, texto) ->
                    DropdownMenuItem(
                        text = { Text(texto) },
                        onClick = {
                            onSeleccionar(valor)
                            expandido = false
                        }
                    )
                }
            }
        }
        if (error != null) {
            Text(
                text = error,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(start = 16.dp, top = 4.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OportunidadFormPreview() {
    CSNexoTheme {
        OportunidadFormScreen(
            state = OportunidadFormUiState(
                nombre = "Patrocinio Feria Empresarial",
                tipo = TipoOportunidad.PATROCINIO,
                descripcion = "Evaluar presencia de marca en el escenario principal.",
                fuenteInformacion = "Fuente ficticia",
                prioridad = Prioridad.ALTA,
                potencial = PotencialComercial.ALTO,
                estado = EstadoOportunidad.EVENTO_IDENTIFICADO,
                nombreError = null
            ),
            onVolver = {},
            onNombreChange = {},
            onTipoChange = {},
            onDescripcionChange = {},
            onFuenteChange = {},
            onPrioridadChange = {},
            onPotencialChange = {},
            onEstadoChange = {},
            onObservacionesChange = {},
            onGuardar = {}
        )
    }
}
