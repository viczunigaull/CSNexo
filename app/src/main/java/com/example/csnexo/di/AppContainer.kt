package com.example.csnexo.di

import android.content.Context
import com.example.csnexo.data.local.AppDatabase
import com.example.csnexo.data.local.seed.DatabaseSeeder
import com.example.csnexo.data.preferences.sesionDataStore
import com.example.csnexo.data.remote.ActivacionServidor
import com.example.csnexo.data.remote.ContactoServidor
import com.example.csnexo.data.remote.EventoServidor
import com.example.csnexo.data.remote.OportunidadServidor
import com.example.csnexo.data.remote.OrganizacionServidor
import com.example.csnexo.data.remote.ResultadoServidor
import com.example.csnexo.data.remote.RetrofitClient
import com.example.csnexo.data.remote.SeguimientoServidor
import com.example.csnexo.data.repository.ActivacionRepositoryImpl
import com.example.csnexo.data.repository.ContactoRepositoryImpl
import com.example.csnexo.data.repository.EventoRepositoryImpl
import com.example.csnexo.data.repository.FakeAuthRepository
import com.example.csnexo.data.repository.OportunidadRepositoryImpl
import com.example.csnexo.data.repository.OrganizacionRepositoryImpl
import com.example.csnexo.data.repository.ResultadoRepositoryImpl
import com.example.csnexo.data.repository.SeguimientoRepositoryImpl
import com.example.csnexo.data.repository.SesionRepositoryImpl
import com.example.csnexo.domain.repository.ActivacionRepository
import com.example.csnexo.domain.repository.AuthRepository
import com.example.csnexo.domain.repository.ContactoRepository
import com.example.csnexo.domain.repository.EventoRepository
import com.example.csnexo.domain.repository.OportunidadRepository
import com.example.csnexo.domain.repository.OrganizacionRepository
import com.example.csnexo.domain.repository.ResultadoRepository
import com.example.csnexo.domain.repository.SeguimientoRepository
import com.example.csnexo.domain.repository.SesionRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

/**
 * Inyección de dependencias manual.
 *
 * La Etapa 2 agrega Room y el repositorio de oportunidades. El sembrado inicial
 * corre en un scope de aplicación y es idempotente.
 */
class AppContainer(context: Context) {

    private val appContext: Context = context.applicationContext
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val authRepository: AuthRepository by lazy { FakeAuthRepository() }

    val sesionRepository: SesionRepository by lazy {
        SesionRepositoryImpl(
            dataStore = appContext.sesionDataStore,
            authRepository = authRepository
        )
    }

    private val database: AppDatabase by lazy {
        AppDatabase.crear(appContext)
    }

    val oportunidadRepository: OportunidadRepository by lazy {
        OportunidadRepositoryImpl(database.oportunidadDao())
    }

    val oportunidadServidor: OportunidadServidor by lazy {
        OportunidadServidor(RetrofitClient.oportunidadApi)
    }

    val organizacionRepository: OrganizacionRepository by lazy {
        OrganizacionRepositoryImpl(database.organizacionDao())
    }

    val organizacionServidor: OrganizacionServidor by lazy {
        OrganizacionServidor(RetrofitClient.organizacionApi)
    }

    val eventoRepository: EventoRepository by lazy {
        EventoRepositoryImpl(database.eventoDao())
    }

    val eventoServidor: EventoServidor by lazy {
        EventoServidor(RetrofitClient.eventoApi)
    }

    val contactoRepository: ContactoRepository by lazy {
        ContactoRepositoryImpl(database.contactoDao())
    }

    val contactoServidor: ContactoServidor by lazy {
        ContactoServidor(RetrofitClient.contactoApi)
    }

    val seguimientoRepository: SeguimientoRepository by lazy {
        SeguimientoRepositoryImpl(database.seguimientoDao())
    }

    val seguimientoServidor: SeguimientoServidor by lazy {
        SeguimientoServidor(RetrofitClient.seguimientoApi)
    }

    val activacionRepository: ActivacionRepository by lazy {
        ActivacionRepositoryImpl(database.activacionDao())
    }

    val activacionServidor: ActivacionServidor by lazy {
        ActivacionServidor(RetrofitClient.activacionApi)
    }

    val resultadoRepository: ResultadoRepository by lazy {
        ResultadoRepositoryImpl(database.resultadoDao())
    }

    val resultadoServidor: ResultadoServidor by lazy {
        ResultadoServidor(RetrofitClient.resultadoApi)
    }

    init {
        applicationScope.launch {
            runCatching {
                DatabaseSeeder(database.oportunidadDao()).sembrarSiVacio()
            }
        }
    }
}
