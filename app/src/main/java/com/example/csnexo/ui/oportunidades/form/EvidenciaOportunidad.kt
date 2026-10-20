package com.example.csnexo.ui.oportunidades.form

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.LocationManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import java.util.Locale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Sección de evidencia de la oportunidad.
 *
 * La cámara se usa para adjuntar una fotografía y la ubicación solo se consulta
 * una vez como referencia de prueba. No hay seguimiento ni mapas.
 */
@Composable
fun EvidenciaOportunidad(
    fotoPath: String?,
    ubicacionReferencia: String?,
    habilitado: Boolean,
    onFotoGuardada: (String) -> Unit,
    onFotoEliminada: () -> Unit,
    onUbicacionObtenida: (String) -> Unit,
    onUbicacionEliminada: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    var mostrarCamara by remember { mutableStateOf(false) }
    var mensaje by remember { mutableStateOf<String?>(null) }

    val lanzadorCamara = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { concedido ->
        if (concedido) {
            mostrarCamara = true
        } else {
            mensaje = "Se necesita permiso de cámara para adjuntar una foto."
        }
    }

    val lanzadorUbicacion = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestMultiplePermissions()
    ) { resultado ->
        if (resultado.values.any { it }) {
            val referencia = obtenerReferenciaUbicacion(context)
            if (referencia != null) {
                onUbicacionObtenida(referencia)
                mensaje = "Referencia de ubicación adjuntada."
            } else {
                mensaje = "No hay una ubicación reciente disponible. Activa la ubicación del dispositivo e intenta nuevamente."
            }
        } else {
            mensaje = "Se necesita permiso de ubicación para obtener la referencia."
        }
    }

    Column(
        modifier = modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "Evidencia (opcional)",
            style = MaterialTheme.typography.titleSmall
        )

        if (fotoPath != null) {
            FotoOportunidad(
                ruta = fotoPath,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(12.dp))
            )
            TextButton(
                onClick = {
                    onFotoEliminada()
                    mensaje = "Foto quitada."
                },
                enabled = habilitado
            ) {
                Text("Quitar foto")
            }
        }

        OutlinedButton(
            onClick = {
                mensaje = null
                when {
                    !context.packageManager.hasSystemFeature(PackageManager.FEATURE_CAMERA_ANY) -> {
                        mensaje = "Este dispositivo no tiene cámara."
                    }

                    context.tienePermiso(Manifest.permission.CAMERA) -> {
                        mostrarCamara = true
                    }

                    else -> {
                        lanzadorCamara.launch(Manifest.permission.CAMERA)
                    }
                }
            },
            enabled = habilitado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (fotoPath == null) {
                    "Tomar foto"
                } else {
                    "Reemplazar foto"
                }
            )
        }

        if (ubicacionReferencia != null) {
            Text(
                text = "Ubicación de referencia: $ubicacionReferencia",
                style = MaterialTheme.typography.bodySmall
            )
            TextButton(
                onClick = {
                    onUbicacionEliminada()
                    mensaje = "Referencia de ubicación quitada."
                },
                enabled = habilitado
            ) {
                Text("Quitar ubicación")
            }
        }

        OutlinedButton(
            onClick = {
                mensaje = null
                if (context.tienePermisoUbicacion()) {
                    val referencia = obtenerReferenciaUbicacion(context)
                    if (referencia != null) {
                        onUbicacionObtenida(referencia)
                        mensaje = "Referencia de ubicación adjuntada."
                    } else {
                        mensaje = "No hay una ubicación reciente disponible. Activa la ubicación del dispositivo e intenta nuevamente."
                    }
                } else {
                    lanzadorUbicacion.launch(
                        arrayOf(
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_COARSE_LOCATION
                        )
                    )
                }
            },
            enabled = habilitado,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = if (ubicacionReferencia == null) {
                    "Usar ubicación de prueba"
                } else {
                    "Actualizar ubicación"
                }
            )
        }

        if (mensaje != null) {
            Text(
                text = mensaje.orEmpty(),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
    }

    if (mostrarCamara) {
        CameraCaptureDialog(
            onDismiss = { mostrarCamara = false },
            onFotoGuardada = { ruta ->
                mostrarCamara = false
                onFotoGuardada(ruta)
                mensaje = "Foto adjuntada."
            }
        )
    }
}

private fun Context.tienePermiso(permiso: String): Boolean =
    ContextCompat.checkSelfPermission(this, permiso) == PackageManager.PERMISSION_GRANTED

private fun Context.tienePermisoUbicacion(): Boolean =
    tienePermiso(Manifest.permission.ACCESS_FINE_LOCATION) ||
        tienePermiso(Manifest.permission.ACCESS_COARSE_LOCATION)

private fun obtenerReferenciaUbicacion(context: Context): String? {
    val manager = context.getSystemService(Context.LOCATION_SERVICE) as? LocationManager
        ?: return null

    val proveedores = listOf(
        LocationManager.GPS_PROVIDER,
        LocationManager.NETWORK_PROVIDER,
        LocationManager.PASSIVE_PROVIDER
    )

    val ubicacion = proveedores
        .mapNotNull { proveedor ->
            runCatching { manager.getLastKnownLocation(proveedor) }.getOrNull()
        }
        .maxByOrNull { it.time }
        ?: return null

    return String.format(
        Locale.US,
        "%.5f, %.5f",
        ubicacion.latitude,
        ubicacion.longitude
    )
}

@Composable
private fun FotoOportunidad(
    ruta: String,
    modifier: Modifier = Modifier
) {
    val imagen by produceState<ImageBitmap?>(initialValue = null, ruta) {
        value = withContext(Dispatchers.IO) {
            decodificarFoto(ruta)?.asImageBitmap()
        }
    }

    val foto = imagen
    if (foto != null) {
        Image(
            bitmap = foto,
            contentDescription = "Evidencia fotográfica de la oportunidad",
            contentScale = ContentScale.Crop,
            modifier = modifier
        )
    } else {
        Box(
            modifier = modifier.background(MaterialTheme.colorScheme.surfaceVariant),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Cargando foto...",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

private fun decodificarFoto(ruta: String): Bitmap? {
    val medidas = BitmapFactory.Options().apply {
        inJustDecodeBounds = true
    }
    BitmapFactory.decodeFile(ruta, medidas)

    if (medidas.outWidth <= 0 || medidas.outHeight <= 0) return null

    var muestra = 1
    while (medidas.outWidth / muestra > 1200 || medidas.outHeight / muestra > 1200) {
        muestra *= 2
    }

    val opciones = BitmapFactory.Options().apply {
        inSampleSize = muestra
    }
    return BitmapFactory.decodeFile(ruta, opciones)
}
