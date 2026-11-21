package com.example.csnexo.backend

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio Spring Data JPA para eventos.
 */
interface EventoRepository : JpaRepository<Evento, Long>
