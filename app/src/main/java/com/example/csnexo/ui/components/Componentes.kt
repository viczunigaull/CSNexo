package com.example.csnexo.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import kotlin.math.roundToInt

/**
 * Campo de texto con error visible, icono opcional y una pequeña sacudida
 * cuando aparece un error. La animación se comenta porque es la parte menos
 * evidente del componente.
 */
@Composable
fun CampoTexto(
    label: String,
    valor: String,
    onCambio: (String) -> Unit,
    numerico: Boolean = false,
    singleLine: Boolean = true,
    error: String? = null,
    icono: ImageVector? = null,
    tipoTeclado: KeyboardType? = null,
    contrasena: Boolean = false,
    enabled: Boolean = true
) {
    val desplazamiento = remember { Animatable(0f) }
    var contrasenaVisible by remember { mutableStateOf(false) }

    // Cuando llega un error, el campo se mueve brevemente para llamar la atención.
    LaunchedEffect(error) {
        if (error != null) {
            repeat(3) {
                desplazamiento.animateTo(10f, tween(45))
                desplazamiento.animateTo(-10f, tween(45))
            }
            desplazamiento.animateTo(0f, tween(45))
        }
    }

    val teclado = tipoTeclado ?: when {
        contrasena -> KeyboardType.Password
        numerico -> KeyboardType.Number
        else -> KeyboardType.Text
    }

    OutlinedTextField(
        value = valor,
        onValueChange = onCambio,
        label = { Text(label) },
        singleLine = singleLine,
        enabled = enabled,
        isError = error != null,
        leadingIcon = if (icono != null) {
            { Icon(icono, contentDescription = null) }
        } else {
            null
        },
        trailingIcon = if (error != null || contrasena) {
            {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (error != null) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = "Campo con error",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                    if (contrasena) {
                        IconButton(onClick = { contrasenaVisible = !contrasenaVisible }) {
                            Icon(
                                imageVector = if (contrasenaVisible) {
                                    Icons.Default.VisibilityOff
                                } else {
                                    Icons.Default.Visibility
                                },
                                contentDescription = if (contrasenaVisible) {
                                    "Ocultar contraseña"
                                } else {
                                    "Mostrar contraseña"
                                }
                            )
                        }
                    }
                }
            }
        } else {
            null
        },
        visualTransformation = if (contrasena && !contrasenaVisible) {
            PasswordVisualTransformation()
        } else {
            VisualTransformation.None
        },
        supportingText = if (error != null) {
            { Text(error) }
        } else {
            null
        },
        keyboardOptions = KeyboardOptions(keyboardType = teclado),
        modifier = Modifier
            .fillMaxWidth()
            .offset { IntOffset(desplazamiento.value.roundToInt(), 0) }
    )
}

@Composable
fun TextoError(error: String?) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error,
                modifier = Modifier.size(18.dp)
            )
            Text(
                text = error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
fun MensajeError(error: String?) {
    AnimatedVisibility(
        visible = error != null,
        enter = fadeIn() + expandVertically(),
        exit = fadeOut() + shrinkVertically()
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Error,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.error
            )
            Text(
                text = error.orEmpty(),
                color = MaterialTheme.colorScheme.error,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun MenuSelector(
    label: String,
    textoActual: String,
    items: List<Pair<String, () -> Unit>>,
    error: String? = null,
    enabled: Boolean = true
) {
    var expandido by remember { mutableStateOf(false) }

    Column(modifier = Modifier.fillMaxWidth()) {
        Text(
            text = label,
            style = MaterialTheme.typography.labelMedium,
            color = if (error != null) {
                MaterialTheme.colorScheme.error
            } else {
                MaterialTheme.colorScheme.onSurface
            }
        )
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expandido = true },
                enabled = enabled,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (error != null) {
                    Icon(
                        imageVector = Icons.Default.Error,
                        contentDescription = "Campo con error",
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(text = "  ")
                }
                Text(text = textoActual, modifier = Modifier.weight(1f))
                Text(text = "▾")
            }
            DropdownMenu(
                expanded = expandido,
                onDismissRequest = { expandido = false }
            ) {
                items.forEach { (texto, accion) ->
                    DropdownMenuItem(
                        text = { Text(texto) },
                        onClick = {
                            accion()
                            expandido = false
                        }
                    )
                }
            }
        }
        TextoError(error)
    }
}

@Composable
fun <T> Selector(
    label: String,
    seleccionado: T?,
    opciones: List<T>,
    etiqueta: (T) -> String,
    onSeleccion: (T) -> Unit,
    vacio: String = "Seleccionar",
    error: String? = null,
    enabled: Boolean = true
) {
    val items = opciones.map { opcion ->
        etiqueta(opcion) to { onSeleccion(opcion) }
    }
    MenuSelector(
        label = label,
        textoActual = if (seleccionado != null) etiqueta(seleccionado) else vacio,
        items = items,
        error = error,
        enabled = enabled
    )
}

/**
 * Tarjeta reutilizable para la lista. La aparición y la escala al presionar son
 * animaciones simples; no afectan la lógica de navegación.
 */
@Composable
fun Tarjeta(
    titulo: String,
    lineas: List<String>,
    onClick: (() -> Unit)? = null,
    destacado: Boolean = false
) {
    val aparecer = remember { MutableTransitionState(false).apply { targetState = true } }
    val interaccion = remember { MutableInteractionSource() }
    val presionada by interaccion.collectIsPressedAsState()
    val escala by animateFloatAsState(
        targetValue = if (presionada) 0.97f else 1f,
        animationSpec = tween(120),
        label = "escalaTarjeta"
    )

    AnimatedVisibility(
        visibleState = aparecer,
        enter = fadeIn(tween(350)) + slideInVertically(tween(350)) { it / 5 }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .scale(escala)
                .animateContentSize()
                .then(
                    if (onClick != null) {
                        Modifier.clickable(
                            interactionSource = interaccion,
                            indication = LocalIndication.current
                        ) { onClick() }
                    } else {
                        Modifier
                    }
                ),
            colors = CardDefaults.cardColors(
                containerColor = if (destacado) {
                    MaterialTheme.colorScheme.errorContainer
                } else {
                    MaterialTheme.colorScheme.surfaceVariant
                }
            )
        ) {
            Column(
                modifier = Modifier.padding(12.dp),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                lineas.filter { it.isNotBlank() }.forEach { linea ->
                    Text(
                        text = linea,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}
