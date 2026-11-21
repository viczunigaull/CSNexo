package com.example.csnexo.data.mapper

import com.example.csnexo.data.local.entity.EventoEntity
import com.example.csnexo.domain.model.Evento
import com.example.csnexo.domain.model.TipoEvento

/**
 * Convierte la entidad Room de evento al modelo de dominio y viceversa.
 */
fun EventoEntity.aDominio(): Evento = Evento(
    id = id,
    nombre = nombre,
    tipo = tipo.toEnum(TipoEvento.OTRA),
    descripcion = descripcion,
    fechaInicio = fechaInicio,
    fechaTermino = fechaTermino,
    comuna = comuna,
    region = region
)

fun Evento.aEntidad(): EventoEntity = EventoEntity(
    id = id,
    nombre = nombre,
    tipo = tipo.name,
    descripcion = descripcion,
    fechaInicio = fechaInicio,
    fechaTermino = fechaTermino,
    comuna = comuna,
    region = region
)

private inline fun <reified T : Enum<T>> String.toEnum(valorPorDefecto: T): T =
    enumValues<T>().firstOrNull { it.name == this } ?: valorPorDefecto
