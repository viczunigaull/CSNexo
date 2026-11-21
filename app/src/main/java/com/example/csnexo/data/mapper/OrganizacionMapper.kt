package com.example.csnexo.data.mapper

import com.example.csnexo.data.local.entity.OrganizacionEntity
import com.example.csnexo.domain.model.Organizacion
import com.example.csnexo.domain.model.TipoOrganizacion

/**
 * Convierte la entidad Room de organización al modelo de dominio y viceversa.
 */
fun OrganizacionEntity.aDominio(): Organizacion = Organizacion(
    id = id,
    nombre = nombre,
    tipo = tipo.toEnum(TipoOrganizacion.OTRA),
    rubro = rubro,
    comuna = comuna,
    region = region
)

fun Organizacion.aEntidad(): OrganizacionEntity = OrganizacionEntity(
    id = id,
    nombre = nombre,
    tipo = tipo.name,
    rubro = rubro,
    comuna = comuna,
    region = region
)

private inline fun <reified T : Enum<T>> String.toEnum(valorPorDefecto: T): T =
    enumValues<T>().firstOrNull { it.name == this } ?: valorPorDefecto
