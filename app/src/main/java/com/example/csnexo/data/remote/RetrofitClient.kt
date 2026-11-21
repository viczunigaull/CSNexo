package com.example.csnexo.data.remote

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Único punto donde se crean los clientes Retrofit.
 *
 * - Microservicio propio: Spring Boot corriendo en la máquina. Desde el
 *   emulador Android, `10.0.2.2` apunta al `localhost` del computador.
 * - API externa: Nager.Date, servicio público de feriados (no requiere clave).
 */
object RetrofitClient {

    // 10.0.2.2 = localhost visto desde el emulador.
    private const val URL_MICROSERVICIO = "http://10.0.2.2:8080/"
    private const val URL_API_EXTERNA = "https://date.nager.at/api/v3/"

    val oportunidadApi: OportunidadApi = Retrofit.Builder()
        .baseUrl(URL_MICROSERVICIO)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OportunidadApi::class.java)

    val organizacionApi: OrganizacionApi = Retrofit.Builder()
        .baseUrl(URL_MICROSERVICIO)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(OrganizacionApi::class.java)

    val eventoApi: EventoApi = Retrofit.Builder()
        .baseUrl(URL_MICROSERVICIO)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(EventoApi::class.java)

    val contactoApi: ContactoApi = Retrofit.Builder()
        .baseUrl(URL_MICROSERVICIO)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(ContactoApi::class.java)

    val seguimientoApi: SeguimientoApi = Retrofit.Builder()
        .baseUrl(URL_MICROSERVICIO)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(SeguimientoApi::class.java)

    val feriadosApi: FeriadosApi = Retrofit.Builder()
        .baseUrl(URL_API_EXTERNA)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
        .create(FeriadosApi::class.java)
}
