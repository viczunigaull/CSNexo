package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Evento
import com.example.csnexo.domain.model.TipoEvento

/**
 * DTO de evento. Refleja el JSON del microservicio.
 */
data class EventoDto(
    val id: Long = 0,
    val nombre: String,
    val tipo: String,
    val descripcion: String,
    val fechaInicio: String,
    val fechaTermino: String,
    val comuna: String,
    val region: String
)

fun EventoDto.aDominio(): Evento = Evento(
    id = id,
    nombre = nombre,
    tipo = tipo.toEnum(TipoEvento.OTRA),
    descripcion = descripcion,
    fechaInicio = fechaInicio,
    fechaTermino = fechaTermino,
    comuna = comuna,
    region = region
)

fun Evento.aDto(): EventoDto = EventoDto(
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
