package com.example.csnexo.backend

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio Spring Data JPA para seguimientos.
 */
interface SeguimientoRepository : JpaRepository<Seguimiento, Long>
