package com.example.csnexo.backend

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio Spring Data JPA. Con solo extender JpaRepository ya se obtienen
 * los métodos CRUD: findAll, findById, save y deleteById.
 */
interface OportunidadRepository : JpaRepository<Oportunidad, Long>
