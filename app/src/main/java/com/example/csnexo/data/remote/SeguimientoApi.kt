package com.example.csnexo.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/**
 * Contrato REST del microservicio de seguimientos.
 */
interface SeguimientoApi {

    @GET("api/seguimientos")
    suspend fun listar(): List<SeguimientoDto>

    @GET("api/seguimientos/{id}")
    suspend fun obtener(@Path("id") id: Long): SeguimientoDto

    @POST("api/seguimientos")
    suspend fun crear(@Body seguimiento: SeguimientoDto): SeguimientoDto

    @PUT("api/seguimientos/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body seguimiento: SeguimientoDto): SeguimientoDto

    @DELETE("api/seguimientos/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
