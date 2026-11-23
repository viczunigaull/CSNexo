package com.example.csnexo.data.remote

import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ResultadoApi {

    @GET("api/resultados")
    suspend fun listar(): List<ResultadoDto>

    @GET("api/resultados/{id}")
    suspend fun obtener(@Path("id") id: Long): ResultadoDto

    @POST("api/resultados")
    suspend fun crear(@Body resultado: ResultadoDto): ResultadoDto

    @PUT("api/resultados/{id}")
    suspend fun actualizar(@Path("id") id: Long, @Body resultado: ResultadoDto): ResultadoDto

    @DELETE("api/resultados/{id}")
    suspend fun eliminar(@Path("id") id: Long)
}
