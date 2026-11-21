package com.example.csnexo.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Contrato REST del microservicio de organizaciones.
 */
interface OrganizacionApi {

    @GET("api/organizaciones")
    suspend fun listar(): List<OrganizacionDto>

    @GET("api/organizaciones/{id}")
    suspend fun obtener(@Path("id") id: Long): OrganizacionDto

    @POST("api/organizaciones")
    suspend fun crear(@Body organizacion: OrganizacionDto): OrganizacionDto

    @PUT("api/organizaciones/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body organizacion: OrganizacionDto): OrganizacionDto

    @DELETE("api/organizaciones/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
