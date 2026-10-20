package com.example.csnexo.di

import android.content.Context
import com.example.csnexo.data.local.AppDatabase
import com.example.csnexo.data.local.seed.DatabaseSeeder
import com.example.csnexo.data.preferences.sesionDataStore
import com.example.csnexo.data.repository.FakeAuthRepository
import com.example.csnexo.data.repository.OportunidadRepositoryImpl
import com.example.csnexo.data.repository.SesionRepositoryImpl
import com.example.csnexo.domain.repository.AuthRepository
import com.example.csnexo.domain.repository.OportunidadRepository
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

    init {
        applicationScope.launch {
            runCatching {
                DatabaseSeeder(database.oportunidadDao()).sembrarSiVacio()
            }
        }
    }
}
