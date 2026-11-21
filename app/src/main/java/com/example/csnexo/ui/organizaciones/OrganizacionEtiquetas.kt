package com.example.csnexo.ui.organizaciones

import com.example.csnexo.domain.model.TipoOrganizacion

/**
 * Etiquetas visibles de los tipos de organización (fuera del dominio).
 */
fun TipoOrganizacion.etiqueta(): String = when (this) {
    TipoOrganizacion.EMPRESA -> "Empresa"
    TipoOrganizacion.MUNICIPALIDAD -> "Municipalidad"
    TipoOrganizacion.INSTITUCION_PUBLICA -> "Institución pública"
    TipoOrganizacion.INSTITUCION_PRIVADA -> "Institución privada"
    TipoOrganizacion.ORGANIZADOR_EVENTOS -> "Organizador de eventos"
    TipoOrganizacion.CENTRO_COMERCIAL -> "Centro comercial"
    TipoOrganizacion.ASOCIACION -> "Asociación"
    TipoOrganizacion.OTRA -> "Otra"
}
