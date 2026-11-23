package com.example.csnexo

import com.example.csnexo.data.local.entity.OportunidadEntity
import com.example.csnexo.data.mapper.aDominio
import com.example.csnexo.data.mapper.aEntidad
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.shouldBe

/**
 * Pruebas del mapper entre la entidad Room y el modelo de dominio.
 * Estilo Kotest (StringSpec), como en la guía del profesor.
 */
class OportunidadMapperTest : StringSpec({

    "entidad a dominio y de vuelta conserva los datos" {
        val entidad = OportunidadEntity(
            id = 5,
            nombre = "Feria",
            tipo = "PATROCINIO",
            descripcion = "d",
            fuenteInformacion = "f",
            prioridad = "ALTA",
            potencialComercial = "ALTO",
            estadoGestion = "EVENTO_IDENTIFICADO",
            observaciones = null,
            fotoPath = null,
            ubicacionReferencia = null,
            creadaEn = 1000L,
            actualizadaEn = 2000L
        )

        val dominio = entidad.aDominio()
        dominio.id shouldBe 5L
        dominio.tipo shouldBe TipoOportunidad.PATROCINIO
        dominio.prioridad shouldBe Prioridad.ALTA

        val deVuelta = dominio.aEntidad()
        deVuelta.tipo shouldBe "PATROCINIO"
        deVuelta.creadaEn shouldBe 1000L
    }

    "enum desconocido usa el valor por defecto" {
        val entidad = OportunidadEntity(
            id = 1,
            nombre = "x",
            tipo = "RARO",
            descripcion = "d",
            fuenteInformacion = "f",
            prioridad = "RARA",
            potencialComercial = "RARA",
            estadoGestion = "RARO",
            observaciones = null,
            fotoPath = null,
            ubicacionReferencia = null,
            creadaEn = 0L,
            actualizadaEn = 0L
        )

        val dominio = entidad.aDominio()
        dominio.tipo shouldBe TipoOportunidad.OTRO
        dominio.prioridad shouldBe Prioridad.MEDIA
    }
})
