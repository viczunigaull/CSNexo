package com.example.csnexo.data.mapper

import com.example.csnexo.data.local.entity.SeguimientoEntity
import com.example.csnexo.domain.model.Seguimiento
import com.example.csnexo.domain.model.TipoAccion

/**
 * Convierte la entidad Room de seguimiento al modelo de dominio y viceversa.
 */
fun SeguimientoEntity.aDominio(): Seguimiento = Seguimiento(
    id = id,
    oportunidad = oportunidad,
    tipoAccion = tipoAccion.toEnum(TipoAccion.SEGUIMIENTO),
    fecha = fecha,
    resultado = resultado
)

fun Seguimiento.aEntidad(): SeguimientoEntity = SeguimientoEntity(
    id = id,
    oportunidad = oportunidad,
    tipoAccion = tipoAccion.name,
    fecha = fecha,
    resultado = resultado
)

private inline fun <reified T : Enum<T>> String.toEnum(valorPorDefecto: T): T =
    enumValues<T>().firstOrNull { it.name == this } ?: valorPorDefecto
