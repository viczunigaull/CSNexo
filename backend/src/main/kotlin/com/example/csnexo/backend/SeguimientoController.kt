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
 * Controlador REST con CRUD de seguimientos.
 */
@RestController
@RequestMapping("/api/seguimientos")
class SeguimientoController(
    private val repository: SeguimientoRepository
) {

    @GetMapping
    fun listar(): List<Seguimiento> = repository.findAll()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long): Seguimiento =
        repository.findById(id).orElseThrow { noEncontrado(id) }

    @PostMapping
    fun crear(@RequestBody seguimiento: Seguimiento): Seguimiento =
        repository.save(seguimiento.copy(id = 0))

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody seguimiento: Seguimiento): Seguimiento {
        val existente = repository.findById(id).orElseThrow { noEncontrado(id) }
        return repository.save(
            existente.copy(
                oportunidad = seguimiento.oportunidad,
                tipoAccion = seguimiento.tipoAccion,
                fecha = seguimiento.fecha,
                resultado = seguimiento.resultado
            )
        )
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) {
        repository.deleteById(id)
    }

    private fun noEncontrado(id: Long) =
        ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el seguimiento $id")
}
