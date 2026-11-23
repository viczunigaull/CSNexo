package com.example.csnexo.data.mapper

import com.example.csnexo.data.local.entity.ActivacionEntity
import com.example.csnexo.domain.model.Activacion

fun ActivacionEntity.aDominio(): Activacion = Activacion(
    id = id,
    oportunidad = oportunidad,
    condiciones = condiciones,
    responsable = responsable
)

fun Activacion.aEntidad(): ActivacionEntity = ActivacionEntity(
    id = id,
    oportunidad = oportunidad,
    condiciones = condiciones,
    responsable = responsable
)
