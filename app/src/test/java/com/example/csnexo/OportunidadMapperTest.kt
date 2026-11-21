package com.example.csnexo

import com.example.csnexo.data.local.entity.OportunidadEntity
import com.example.csnexo.data.mapper.aDominio
import com.example.csnexo.data.mapper.aEntidad
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import org.junit.Assert.assertEquals
import org.junit.Test

/**
 * Pruebas del mapper entre la entidad Room y el modelo de dominio.
 */
class OportunidadMapperTest {

    @Test
    fun `entidad a dominio y de vuelta conserva los datos`() {
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
        assertEquals(5L, dominio.id)
        assertEquals(TipoOportunidad.PATROCINIO, dominio.tipo)
        assertEquals(Prioridad.ALTA, dominio.prioridad)

        val deVuelta = dominio.aEntidad()
        assertEquals("PATROCINIO", deVuelta.tipo)
        assertEquals(1000L, deVuelta.creadaEn)
    }

    @Test
    fun `enum desconocido usa el valor por defecto`() {
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
        assertEquals(TipoOportunidad.OTRO, dominio.tipo)
        assertEquals(Prioridad.MEDIA, dominio.prioridad)
    }
}
