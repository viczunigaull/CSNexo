package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Seguimiento
import com.example.csnexo.domain.model.TipoAccion

/**
 * DTO de seguimiento. Refleja el JSON del microservicio.
 */
data class SeguimientoDto(
    val id: Long = 0,
    val oportunidad: String,
    val tipoAccion: String,
    val fecha: String,
    val resultado: String
)

fun SeguimientoDto.aDominio(): Seguimiento = Seguimiento(
    id = id,
    oportunidad = oportunidad,
    tipoAccion = tipoAccion.toEnum(TipoAccion.SEGUIMIENTO),
    fecha = fecha,
    resultado = resultado
)

fun Seguimiento.aDto(): SeguimientoDto = SeguimientoDto(
    id = id,
    oportunidad = oportunidad,
    tipoAccion = tipoAccion.name,
    fecha = fecha,
    resultado = resultado
)

private inline fun <reified T : Enum<T>> String.toEnum(valorPorDefecto: T): T =
    enumValues<T>().firstOrNull { it.name == this } ?: valorPorDefecto
