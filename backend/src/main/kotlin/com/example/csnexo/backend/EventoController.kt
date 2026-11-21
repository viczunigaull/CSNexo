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
 * Controlador REST con CRUD de eventos.
 *
 *  - GET    /api/eventos        -> listar
 *  - GET    /api/eventos/{id}   -> obtener
 *  - POST   /api/eventos        -> crear
 *  - PUT    /api/eventos/{id}   -> actualizar
 *  - DELETE /api/eventos/{id}   -> eliminar
 */
@RestController
@RequestMapping("/api/eventos")
class EventoController(
    private val repository: EventoRepository
) {

    @GetMapping
    fun listar(): List<Evento> = repository.findAll()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long): Evento =
        repository.findById(id).orElseThrow { noEncontrado(id) }

    @PostMapping
    fun crear(@RequestBody evento: Evento): Evento =
        repository.save(evento.copy(id = 0))

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody evento: Evento): Evento {
        val existente = repository.findById(id).orElseThrow { noEncontrado(id) }
        return repository.save(
            existente.copy(
                nombre = evento.nombre,
                tipo = evento.tipo,
                descripcion = evento.descripcion,
                fechaInicio = evento.fechaInicio,
                fechaTermino = evento.fechaTermino,
                comuna = evento.comuna,
                region = evento.region
            )
        )
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) {
        repository.deleteById(id)
    }

    private fun noEncontrado(id: Long) =
        ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el evento $id")
}
