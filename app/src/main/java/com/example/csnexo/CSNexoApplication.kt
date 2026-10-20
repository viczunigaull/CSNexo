package com.example.csnexo

import android.app.Application
import com.example.csnexo.di.AppContainer

/**
 * Punto de composición de la aplicación.
 *
 * El contenedor se crea una sola vez y será el encargado de construir la base de
 * datos y los repositorios a medida que se implementen las siguientes etapas.
 */
class CSNexoApplication : Application() {

    lateinit var container: AppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = AppContainer(this)
    }
}
