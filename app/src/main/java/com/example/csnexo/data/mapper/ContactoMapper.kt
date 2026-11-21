package com.example.csnexo.data.mapper

import com.example.csnexo.data.local.entity.ContactoEntity
import com.example.csnexo.domain.model.AreaContacto
import com.example.csnexo.domain.model.Contacto

/**
 * Convierte la entidad Room de contacto al modelo de dominio y viceversa.
 */
fun ContactoEntity.aDominio(): Contacto = Contacto(
    id = id,
    nombre = nombre,
    organizacion = organizacion,
    cargo = cargo,
    area = area.toEnum(AreaContacto.ADMINISTRACION),
    correo = correo,
    telefono = telefono
)

fun Contacto.aEntidad(): ContactoEntity = ContactoEntity(
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
