package com.example.csnexo.ui.resultados.list

import com.example.csnexo.domain.model.Resultado

data class ResultadoListaUiState(
    val cargando: Boolean = true,
    val resultados: List<Resultado> = emptyList(),
    val error: String? = null,
    val sincronizando: Boolean = false,
    val mensaje: String? = null
)
