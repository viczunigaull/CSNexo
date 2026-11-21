package com.example.csnexo.backend

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException

/**
 * Controlador REST con las operaciones CRUD de oportunidades.
 *
 * Rutas:
 *  - GET    /api/oportunidades        -> listar todas
 *  - GET    /api/oportunidades/{id}   -> obtener una
 *  - POST   /api/oportunidades        -> crear
 *  - PUT    /api/oportunidades/{id}   -> actualizar
 *  - DELETE /api/oportunidades/{id}   -> eliminar
 */
@RestController
@RequestMapping("/api/oportunidades")
class OportunidadController(
    private val repository: OportunidadRepository
) {

    @GetMapping
    fun listar(): List<Oportunidad> = repository.findAll()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long): Oportunidad =
        repository.findById(id).orElseThrow { noEncontrada(id) }

    @PostMapping
    fun crear(@RequestBody oportunidad: Oportunidad): Oportunidad {
        val ahora = System.currentTimeMillis()
        val nueva = oportunidad.copy(id = 0, creadaEn = ahora, actualizadaEn = ahora)
        return repository.save(nueva)
    }

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody oportunidad: Oportunidad): Oportunidad {
        val existente = repository.findById(id).orElseThrow { noEncontrada(id) }
        val actualizada = existente.copy(
            nombre = oportunidad.nombre,
            tipo = oportunidad.tipo,
            descripcion = oportunidad.descripcion,
            fuenteInformacion = oportunidad.fuenteInformacion,
            prioridad = oportunidad.prioridad,
            potencialComercial = oportunidad.potencialComercial,
            estadoGestion = oportunidad.estadoGestion,
            observaciones = oportunidad.observaciones,
            actualizadaEn = System.currentTimeMillis()
        )
        return repository.save(actualizada)
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) {
        repository.deleteById(id)
    }

    private fun noEncontrada(id: Long) =
        ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la oportunidad $id")
}
