package com.example.csnexo.data.mapper

import com.example.csnexo.data.local.entity.ResultadoEntity
import com.example.csnexo.domain.model.Resultado

fun ResultadoEntity.aDominio(): Resultado = Resultado(
    id = id,
    oportunidad = oportunidad,
    resultado = resultado,
    observaciones = observaciones
)

fun Resultado.aEntidad(): ResultadoEntity = ResultadoEntity(
    id = id,
    oportunidad = oportunidad,
    resultado = resultado,
    observaciones = observaciones
)
