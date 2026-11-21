package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Organizacion
import com.example.csnexo.domain.model.TipoOrganizacion

/**
 * DTO de organización. Refleja el JSON del microservicio; el enum viaja como texto.
 */
data class OrganizacionDto(
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val rubro: String,
    val comuna: String,
    val region: String
)

fun OrganizacionDto.aDominio(): Organizacion = Organizacion(
    id = id,
    nombre = nombre,
    tipo = tipo.toEnum(TipoOrganizacion.OTRA),
    rubro = rubro,
    comuna = comuna,
    region = region
)

fun Organizacion.aDto(): OrganizacionDto = OrganizacionDto(
    id = id,
    nombre = nombre,
    tipo = tipo.name,
    rubro = rubro,
    comuna = comuna,
    region = region
)

private inline fun <reified T : Enum<T>> String.toEnum(valorPorDefecto: T): T =
    enumValues<T>().firstOrNull { it.name == this } ?: valorPorDefecto
