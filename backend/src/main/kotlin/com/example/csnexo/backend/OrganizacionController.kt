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
 * Controlador REST con CRUD de organizaciones.
 *
 *  - GET    /api/organizaciones        -> listar
 *  - GET    /api/organizaciones/{id}   -> obtener
 *  - POST   /api/organizaciones        -> crear
 *  - PUT    /api/organizaciones/{id}   -> actualizar
 *  - DELETE /api/organizaciones/{id}   -> eliminar
 */
@RestController
@RequestMapping("/api/organizaciones")
class OrganizacionController(
    private val repository: OrganizacionRepository
) {

    @GetMapping
    fun listar(): List<Organizacion> = repository.findAll()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long): Organizacion =
        repository.findById(id).orElseThrow { noEncontrada(id) }

    @PostMapping
    fun crear(@RequestBody organizacion: Organizacion): Organizacion =
        repository.save(organizacion.copy(id = 0))

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody organizacion: Organizacion): Organizacion {
        val existente = repository.findById(id).orElseThrow { noEncontrada(id) }
        return repository.save(
            existente.copy(
                nombre = organizacion.nombre,
                tipo = organizacion.tipo,
                rubro = organizacion.rubro,
                comuna = organizacion.comuna,
                region = organizacion.region
            )
        )
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) {
        repository.deleteById(id)
    }

    private fun noEncontrada(id: Long) =
        ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la organización $id")
}
