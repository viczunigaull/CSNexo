package com.example.csnexo.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Contrato REST del microservicio de eventos.
 */
interface EventoApi {

    @GET("api/eventos")
    suspend fun listar(): List<EventoDto>

    @GET("api/eventos/{id}")
    suspend fun obtener(@Path("id") id: Long): EventoDto

    @POST("api/eventos")
    suspend fun crear(@Body evento: EventoDto): EventoDto

    @PUT("api/eventos/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body evento: EventoDto): EventoDto

    @DELETE("api/eventos/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
