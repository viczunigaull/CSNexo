package com.example.csnexo.backend

import org.springframework.data.jpa.repository.JpaRepository

/**
 * Repositorio Spring Data JPA para organizaciones. Extender JpaRepository da los
 * métodos CRUD automáticamente (findAll, findById, save, deleteById).
 */
interface OrganizacionRepository : JpaRepository<Organizacion, Long>
