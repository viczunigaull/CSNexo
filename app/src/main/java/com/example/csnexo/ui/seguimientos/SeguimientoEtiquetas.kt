package com.example.csnexo.ui.seguimientos

import com.example.csnexo.domain.model.TipoAccion

/**
 * Etiquetas visibles de los tipos de acción de seguimiento.
 */
fun TipoAccion.etiqueta(): String = when (this) {
    TipoAccion.INVESTIGACION_INICIAL -> "Investigación inicial"
    TipoAccion.BUSQUEDA_CONTACTO -> "Búsqueda de contacto"
    TipoAccion.PRIMER_CONTACTO -> "Primer contacto"
    TipoAccion.ENVIO_PROPUESTA -> "Envío de propuesta"
    TipoAccion.REUNION -> "Reunión"
    TipoAccion.LLAMADA -> "Llamada"
    TipoAccion.SEGUIMIENTO -> "Seguimiento"
    TipoAccion.CIERRE -> "Cierre"
}
