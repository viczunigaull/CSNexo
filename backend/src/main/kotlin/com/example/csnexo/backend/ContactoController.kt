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
 * Controlador REST con CRUD de contactos.
 */
@RestController
@RequestMapping("/api/contactos")
class ContactoController(
    private val repository: ContactoRepository
) {

    @GetMapping
    fun listar(): List<Contacto> = repository.findAll()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long): Contacto =
        repository.findById(id).orElseThrow { noEncontrado(id) }

    @PostMapping
    fun crear(@RequestBody contacto: Contacto): Contacto =
        repository.save(contacto.copy(id = 0))

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody contacto: Contacto): Contacto {
        val existente = repository.findById(id).orElseThrow { noEncontrado(id) }
        return repository.save(
            existente.copy(
                nombre = contacto.nombre,
                organizacion = contacto.organizacion,
                cargo = contacto.cargo,
                area = contacto.area,
                correo = contacto.correo,
                telefono = contacto.telefono
            )
        )
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) {
        repository.deleteById(id)
    }

    private fun noEncontrado(id: Long) =
        ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el contacto $id")
}
