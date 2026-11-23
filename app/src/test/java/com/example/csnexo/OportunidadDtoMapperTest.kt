package com.example.csnexo

import com.example.csnexo.data.remote.OportunidadDto
import com.example.csnexo.data.remote.aDominio
import com.example.csnexo.data.remote.aDto
import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldBeNull
import io.kotest.matchers.shouldBe
import java.time.Instant

/**
 * Pruebas del mapper entre el DTO del microservicio y el modelo de dominio.
 */
class OportunidadDtoMapperTest : StringSpec({

    "dto a dominio y de vuelta conserva los datos" {
        val dto = OportunidadDto(
            id = 3,
            nombre = "Feria",
            tipo = "CONVENIO",
            descripcion = "d",
            fuenteInformacion = "f",
            prioridad = "BAJA",
            potencialComercial = "BAJO",
            estadoGestion = "CONTACTO_PENDIENTE",
            observaciones = "",
            creadaEn = 1000L,
            actualizadaEn = 2000L
        )

        val dominio = dto.aDominio()
        dominio.tipo shouldBe TipoOportunidad.CONVENIO
        dominio.prioridad shouldBe Prioridad.BAJA

        val deVuelta = dominio.aDto()
        deVuelta.tipo shouldBe "CONVENIO"
        deVuelta.observaciones.shouldBeNull()
    }

    "dominio a dto mantiene fechas en epoch millis" {
        val dominio = Oportunidad(
            id = 7,
            nombre = "x",
            tipo = TipoOportunidad.OTRO,
            descripcion = "d",
            fuenteInformacion = "f",
            prioridad = Prioridad.MEDIA,
            potencialComercial = PotencialComercial.MEDIO,
            estadoGestion = EstadoOportunidad.EVENTO_IDENTIFICADO,
            creadaEn = Instant.ofEpochMilli(1234L),
            actualizadaEn = Instant.ofEpochMilli(5678L)
        )

        val dto = dominio.aDto()
        dto.creadaEn shouldBe 1234L
        dto.actualizadaEn shouldBe 5678L
    }
})
