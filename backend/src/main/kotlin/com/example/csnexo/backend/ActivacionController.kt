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

@RestController
@RequestMapping("/api/activaciones")
class ActivacionController(
    private val repository: ActivacionRepository
) {

    @GetMapping
    fun listar(): List<Activacion> = repository.findAll()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long): Activacion =
        repository.findById(id).orElseThrow { noEncontrada(id) }

    @PostMapping
    fun crear(@RequestBody activacion: Activacion): Activacion =
        repository.save(activacion.copy(id = 0))

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody activacion: Activacion): Activacion {
        val existente = repository.findById(id).orElseThrow { noEncontrada(id) }
        return repository.save(
            existente.copy(
                oportunidad = activacion.oportunidad,
                condiciones = activacion.condiciones,
                responsable = activacion.responsable
            )
        )
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) {
        repository.deleteById(id)
    }

    private fun noEncontrada(id: Long) =
        ResponseStatusException(HttpStatus.NOT_FOUND, "No existe la activación $id")
}
