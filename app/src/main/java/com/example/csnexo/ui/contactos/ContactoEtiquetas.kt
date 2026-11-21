package com.example.csnexo.ui.contactos

import com.example.csnexo.domain.model.AreaContacto

/**
 * Etiquetas visibles de las áreas de contacto.
 */
fun AreaContacto.etiqueta(): String = when (this) {
    AreaContacto.EVENTOS -> "Eventos"
    AreaContacto.ACTIVACIONES -> "Activaciones"
    AreaContacto.MARKETING -> "Marketing"
    AreaContacto.COMUNICACIONES -> "Comunicaciones"
    AreaContacto.RECURSOS_HUMANOS -> "Recursos Humanos"
    AreaContacto.BIENESTAR -> "Bienestar"
    AreaContacto.BENEFICIOS -> "Beneficios"
    AreaContacto.ADMINISTRACION -> "Administración"
}
