package com.example.csnexo.backend

import org.springframework.boot.CommandLineRunner
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

/**
 * Siembra un par de oportunidades ficticias la primera vez que arranca el
 * servicio, para que la app tenga datos que mostrar. No usa datos reales.
 */
@Configuration
class DatosIniciales {

    @Bean
    fun sembrar(repository: OportunidadRepository): CommandLineRunner =
        CommandLineRunner {
            if (repository.count() == 0L) {
                val ahora = System.currentTimeMillis()
                val dia = 24L * 60L * 60L * 1000L
                repository.saveAll(
                    listOf(
                        Oportunidad(
                            nombre = "Patrocinio Feria Empresarial del Maule",
                            tipo = "PATROCINIO",
                            descripcion = "Evaluar patrocinio del escenario principal y presencia de marca.",
                            fuenteInformacion = "Cámara de Comercio ficticia",
                            prioridad = "ALTA",
                            potencialComercial = "ALTO",
                            estadoGestion = "EVENTO_IDENTIFICADO",
                            observaciones = "Registro ficticio de demostración.",
                            creadaEn = ahora - 2 * dia,
                            actualizadaEn = ahora - dia
                        ),
                        Oportunidad(
                            nombre = "Charla de beneficios para pymes",
                            tipo = "PONENCIA",
                            descripcion = "Proponer una charla sobre beneficios corporativos para pequeñas empresas.",
                            fuenteInformacion = "Municipalidad ficticia de prueba",
                            prioridad = "MEDIA",
                            potencialComercial = "MEDIO",
                            estadoGestion = "CONTACTO_PENDIENTE",
                            observaciones = "Falta identificar a la persona de contacto.",
                            creadaEn = ahora - 4 * dia,
                            actualizadaEn = ahora - 2 * dia
                        )
                    )
                )
            }
        }
}
