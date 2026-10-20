package com.example.csnexo.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.csnexo.CSNexoApplication
import com.example.csnexo.ui.auth.LoginViewModel
import com.example.csnexo.ui.auth.SesionViewModel
import com.example.csnexo.ui.oportunidades.list.ListaOportunidadesViewModel

/**
 * Fábrica central de ViewModels.
 *
 * La construcción de dependencias queda en `AppContainer`; los composables solo
 * piden el ViewModel. A medida que se agreguen pantallas, se suman initializers.
 */
object AppViewModelProvider {

    val Factory = viewModelFactory {
        initializer {
            LoginViewModel(
                sesionRepository = csnexoApplication().container.sesionRepository
            )
        }
        initializer {
            SesionViewModel(
                repository = csnexoApplication().container.sesionRepository
            )
        }
        initializer {
            ListaOportunidadesViewModel(
                repository = csnexoApplication().container.oportunidadRepository
            )
        }
    }
}

private fun CreationExtras.csnexoApplication(): CSNexoApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CSNexoApplication
