package com.example.csnexo.ui.oportunidades

import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad

/**
 * Etiquetas visibles. Se mantienen fuera del dominio para que los enums no
 * dependan de textos de interfaz.
 */
fun TipoOportunidad.etiqueta(): String = when (this) {
    TipoOportunidad.PATROCINIO -> "Patrocinio"
    TipoOportunidad.STAND_FERIA -> "Stand en feria"
    TipoOportunidad.PONENCIA -> "Ponencia"
    TipoOportunidad.CONVENIO -> "Convenio"
    TipoOportunidad.ACTIVACION -> "Activación"
    TipoOportunidad.OTRO -> "Otro"
}

fun Prioridad.etiqueta(): String = when (this) {
    Prioridad.ALTA -> "Alta"
    Prioridad.MEDIA -> "Media"
    Prioridad.BAJA -> "Baja"
}

fun PotencialComercial.etiqueta(): String = when (this) {
    PotencialComercial.ALTO -> "Alto"
    PotencialComercial.MEDIO -> "Medio"
    PotencialComercial.BAJO -> "Bajo"
}

fun EstadoOportunidad.etiqueta(): String = when (this) {
    EstadoOportunidad.EVENTO_IDENTIFICADO -> "Evento identificado"
    EstadoOportunidad.CONTACTO_PENDIENTE -> "Contacto pendiente"
    EstadoOportunidad.CONTACTO_REALIZADO -> "Contacto realizado"
    EstadoOportunidad.PROPUESTA_ENVIADA -> "Propuesta enviada"
    EstadoOportunidad.EN_ESPERA_RESPUESTA -> "En espera de respuesta"
    EstadoOportunidad.PARTICIPACION_CONFIRMADA -> "Participación confirmada"
    EstadoOportunidad.DESCARTADA -> "Oportunidad descartada"
}
