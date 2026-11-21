package com.example.csnexo.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

/**
 * Punto de entrada del microservicio de oportunidades.
 *
 * Levanta un servidor REST en http://localhost:8080 con los endpoints de
 * `/api/oportunidades`. Se ejecuta con: `./gradlew bootRun`.
 */
@SpringBootApplication
class CsnexoBackendApplication

fun main(args: Array<String>) {
    runApplication<CsnexoBackendApplication>(*args)
}
