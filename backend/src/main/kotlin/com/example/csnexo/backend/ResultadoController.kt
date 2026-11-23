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
@RequestMapping("/api/resultados")
class ResultadoController(
    private val repository: ResultadoRepository
) {

    @GetMapping
    fun listar(): List<Resultado> = repository.findAll()

    @GetMapping("/{id}")
    fun obtener(@PathVariable id: Long): Resultado =
        repository.findById(id).orElseThrow { noEncontrado(id) }

    @PostMapping
    fun crear(@RequestBody resultado: Resultado): Resultado =
        repository.save(resultado.copy(id = 0))

    @PutMapping("/{id}")
    fun actualizar(@PathVariable id: Long, @RequestBody resultado: Resultado): Resultado {
        val existente = repository.findById(id).orElseThrow { noEncontrado(id) }
        return repository.save(
            existente.copy(
                oportunidad = resultado.oportunidad,
                resultado = resultado.resultado,
                observaciones = resultado.observaciones
            )
        )
    }

    @DeleteMapping("/{id}")
    fun eliminar(@PathVariable id: Long) {
        repository.deleteById(id)
    }

    private fun noEncontrado(id: Long) =
        ResponseStatusException(HttpStatus.NOT_FOUND, "No existe el resultado $id")
}
