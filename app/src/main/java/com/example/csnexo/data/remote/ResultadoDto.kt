package com.example.csnexo.data.remote

import com.example.csnexo.domain.model.Resultado

data class ResultadoDto(
    val id: Long = 0,
    val oportunidad: String,
    val resultado: String,
    val observaciones: String
)

fun ResultadoDto.aDominio(): Resultado = Resultado(
    id = id,
    oportunidad = oportunidad,
    resultado = resultado,
    observaciones = observaciones
)

fun Resultado.aDto(): ResultadoDto = ResultadoDto(
    id = id,
    oportunidad = oportunidad,
    resultado = resultado,
    observaciones = observaciones
)
