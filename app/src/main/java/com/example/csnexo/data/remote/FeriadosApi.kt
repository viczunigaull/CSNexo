package com.example.csnexo.data.remote

import retrofit2.http.GET
import retrofit2.http.Path

/**
 * Contrato de la API externa de feriados públicos.
 *
 * Es un ejemplo de "consumo de API externa" pedido en la rúbrica: una fuente de
 * datos ajena a los microservicios propios, consumida vía Retrofit.
 */
interface FeriadosApi {

    @GET("PublicHolidays/{anio}/CL")
    suspend fun feriados(@Path("anio") anio: Int): List<FeriadoDto>
}
