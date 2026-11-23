package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Activacion

data class ActivacionDto(
    val id: Long = 0,
    val oportunidad: String,
    val condiciones: String,
    val responsable: String
)

fun ActivacionDto.aDominio(): Activacion = Activacion(
    id = id,
    oportunidad = oportunidad,
    condiciones = condiciones,
    responsable = responsable
)

fun Activacion.aDto(): ActivacionDto = ActivacionDto(
    id = id,
    oportunidad = oportunidad,
    condiciones = condiciones,
    responsable = responsable
)
