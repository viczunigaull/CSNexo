package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.AreaContacto
import com.example.csnexo.domain.model.Contacto

/**
 * DTO de contacto. Refleja el JSON del microservicio.
 */
data class ContactoDto(
    val id: Long = 0,
    val nombre: String,
    val organizacion: String,
    val cargo: String,
    val area: String,
    val correo: String,
    val telefono: String
)

fun ContactoDto.aDominio(): Contacto = Contacto(
    id = id,
    nombre = nombre,
    organizacion = organizacion,
    cargo = cargo,
    area = area.toEnum(AreaContacto.ADMINISTRACION),
    correo = correo,
    telefono = telefono
)

fun Contacto.aDto(): ContactoDto = ContactoDto(
    id = id,
    nombre = nombre,
    organizacion = organizacion,
    cargo = cargo,
    area = area.name,
    correo = correo,
    telefono = telefono
)

private inline fun <reified T : Enum<T>> String.toEnum(valorPorDefecto: T): T =
    enumValues<T>().firstOrNull { it.name == this } ?: valorPorDefecto
