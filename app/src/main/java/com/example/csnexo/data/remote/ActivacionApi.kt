package com.example.csnexo.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ActivacionApi {

    @GET("api/activaciones")
    suspend fun listar(): List<ActivacionDto>

    @GET("api/activaciones/{id}")
    suspend fun obtener(@Path("id") id: Long): ActivacionDto

    @POST("api/activaciones")
    suspend fun crear(@Body activacion: ActivacionDto): ActivacionDto

    @PUT("api/activaciones/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body activacion: ActivacionDto): ActivacionDto

    @DELETE("api/activaciones/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
