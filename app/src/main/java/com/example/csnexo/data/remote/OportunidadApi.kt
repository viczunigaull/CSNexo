package com.example.csnexo.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Contrato REST del microservicio de oportunidades (Spring Boot).
 *
 * Retrofit convierte cada método en una llamada HTTP a la URL base configurada
 * en [RetrofitClient]. Los métodos suspend se ejecutan en un hilo de fondo.
 */
interface OportunidadApi {

    @GET("api/oportunidades")
    suspend fun listar(): List<OportunidadDto>

    @GET("api/oportunidades/{id}")
    suspend fun obtener(@Path("id") id: Long): OportunidadDto

    @POST("api/oportunidades")
    suspend fun crear(@Body oportunidad: OportunidadDto): OportunidadDto

    @PUT("api/oportunidades/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body oportunidad: OportunidadDto): OportunidadDto

    @DELETE("api/oportunidades/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
