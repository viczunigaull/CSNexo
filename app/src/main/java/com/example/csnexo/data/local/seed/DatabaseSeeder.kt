package com.example.csnexo.data.local.seed

import com.example.csnexo.data.local.dao.OportunidadDao
import com.example.csnexo.data.local.entity.OportunidadEntity
import com.example.csnexo.domain.model.EstadoOportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad

/**
 * Datos de demostración para que la lista tenga contenido en la primera
 * ejecución. Todos los nombres y fuentes son ficticios.
 */
class DatabaseSeeder(
    private val dao: OportunidadDao
) {

    suspend fun sembrarSiVacio() {
        if (dao.contar() > 0) return

        val ahora = System.currentTimeMillis()
        val dia = 24L * 60L * 60L * 1000L

        dao.insertarTodas(
            listOf(
                OportunidadEntity(
                    nombre = "Patrocinio Feria Empresarial del Maule",
                    tipo = TipoOportunidad.PATROCINIO.name,
                    descripcion = "Evaluar patrocinio del escenario principal y presencia de marca.",
                    fuenteInformacion = "Cámara de Comercio ficticia",
                    prioridad = Prioridad.ALTA.name,
                    potencialComercial = PotencialComercial.ALTO.name,
                    estadoGestion = EstadoOportunidad.EVENTO_IDENTIFICADO.name,
                    observaciones = "Registro ficticio de demostración.",
                    fotoPath = null,
                    ubicacionReferencia = null,
                    creadaEn = ahora - (2 * dia),
                    actualizadaEn = ahora - dia
                ),
                OportunidadEntity(
                    nombre = "Charla de beneficios para pymes",
                    tipo = TipoOportunidad.PONENCIA.name,
                    descripcion = "Proponer una charla sobre beneficios corporativos para pequeñas empresas.",
                    fuenteInformacion = "Municipalidad ficticia de prueba",
                    prioridad = Prioridad.MEDIA.name,
                    potencialComercial = PotencialComercial.MEDIO.name,
                    estadoGestion = EstadoOportunidad.CONTACTO_PENDIENTE.name,
                    observaciones = "Falta identificar a la persona de contacto.",
                    fotoPath = null,
                    ubicacionReferencia = null,
                    creadaEn = ahora - (4 * dia),
                    actualizadaEn = ahora - (2 * dia)
                )
            )
        )
    }
}
