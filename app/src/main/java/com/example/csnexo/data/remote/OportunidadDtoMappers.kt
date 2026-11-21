package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import java.time.Instant

/**
 * Conversión entre el DTO del microservicio y el modelo de dominio.
 */
fun OportunidadDto.aDominio(): Oportunidad = Oportunidad(
    id = id,
    nombre = nombre,
    tipo = tipo.toEnum(TipoOportunidad.OTRO),
    descripcion = descripcion,
    fuenteInformacion = fuenteInformacion,
    prioridad = prioridad.toEnum(Prioridad.MEDIA),
    potencialComercial = potencialComercial.toEnum(PotencialComercial.MEDIO),
    estadoGestion = estadoGestion.toEnum(EstadoOportunidad.EVENTO_IDENTIFICADO),
    observaciones = observaciones.orEmpty(),
    fotoPath = null,
    ubicacionReferencia = null,
    creadaEn = Instant.ofEpochMilli(creadaEn),
    actualizadaEn = Instant.ofEpochMilli(actualizadaEn)
)

fun Oportunidad.aDto(): OportunidadDto = OportunidadDto(
    id = id,
    nombre = nombre,
    tipo = tipo.name,
    descripcion = descripcion,
    fuenteInformacion = fuenteInformacion,
    prioridad = prioridad.name,
    potencialComercial = potencialComercial.name,
    estadoGestion = estadoGestion.name,
    observaciones = observaciones.ifBlank { null },
    creadaEn = creadaEn.toEpochMilli(),
    actualizadaEn = actualizadaEn.toEpochMilli()
)

// Búsqueda tolerante del enum guardado como texto.
private inline fun <reified T : Enum<T>> String.toEnum(valorPorDefecto: T): T =
    enumValues<T>().firstOrNull { it.name == this } ?: valorPorDefecto
