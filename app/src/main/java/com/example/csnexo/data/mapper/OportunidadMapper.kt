package com.example.csnexo.data.mapper

import com.example.csnexo.data.local.entity.OportunidadEntity
import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import java.time.Instant

/**
 * Convierte la entidad de Room al modelo que usan los ViewModels.
 * Si un valor de enum no se reconoce, se usa un valor por defecto para no
 * romper la pantalla por un dato antiguo o corrupto.
 */
fun OportunidadEntity.aDominio(): Oportunidad = Oportunidad(
    id = id,
    nombre = nombre,
    tipo = tipo.toEnum(TipoOportunidad.OTRO),
    descripcion = descripcion,
    fuenteInformacion = fuenteInformacion,
    prioridad = prioridad.toEnum(Prioridad.MEDIA),
    potencialComercial = potencialComercial.toEnum(PotencialComercial.MEDIO),
    estadoGestion = estadoGestion.toEnum(EstadoOportunidad.EVENTO_IDENTIFICADO),
    observaciones = observaciones.orEmpty(),
    fotoPath = fotoPath,
    ubicacionReferencia = ubicacionReferencia,
    creadaEn = Instant.ofEpochMilli(creadaEn),
    actualizadaEn = Instant.ofEpochMilli(actualizadaEn)
)

/**
 * Convierte el modelo de dominio a la entidad que se guarda en Room.
 * Las fechas se almacenan como epoch millis para que Room no dependa de java.time.
 */
fun Oportunidad.aEntidad(): OportunidadEntity = OportunidadEntity(
    id = id,
    nombre = nombre,
    tipo = tipo.name,
    descripcion = descripcion,
    fuenteInformacion = fuenteInformacion,
    prioridad = prioridad.name,
    potencialComercial = potencialComercial.name,
    estadoGestion = estadoGestion.name,
    observaciones = observaciones.ifBlank { null },
    fotoPath = fotoPath,
    ubicacionReferencia = ubicacionReferencia,
    creadaEn = creadaEn.toEpochMilli(),
    actualizadaEn = actualizadaEn.toEpochMilli()
)

// Búsqueda tolerante del enum guardado como texto.
private inline fun <reified T : Enum<T>> String.toEnum(valorPorDefecto: T): T =
    enumValues<T>().firstOrNull { it.name == this } ?: valorPorDefecto
