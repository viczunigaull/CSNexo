package com.example.csnexo

import com.example.csnexo.data.remote.OportunidadDto
import com.example.csnexo.data.remote.aDominio
import com.example.csnexo.data.remote.aDto
import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import java.time.Instant
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

/**
 * Pruebas del mapper entre el DTO del microservicio y el modelo de dominio.
 */
class OportunidadDtoMapperTest {

    @Test
    fun `dto a dominio y de vuelta conserva los datos`() {
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
        assertEquals(TipoOportunidad.CONVENIO, dominio.tipo)
        assertEquals(Prioridad.BAJA, dominio.prioridad)

        val deVuelta = dominio.aDto()
        assertEquals("CONVENIO", deVuelta.tipo)
        assertNull(deVuelta.observaciones)
    }

    @Test
    fun `dominio a dto mantiene fechas en epoch millis`() {
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
        assertEquals(1234L, dto.creadaEn)
        assertEquals(5678L, dto.actualizadaEn)
    }
}
