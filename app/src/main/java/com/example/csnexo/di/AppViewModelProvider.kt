package com.example.csnexo.di

import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.csnexo.CSNexoApplication
import com.example.csnexo.data.remote.RetrofitClient
import com.example.csnexo.ui.auth.LoginViewModel
import com.example.csnexo.ui.auth.SesionViewModel
import com.example.csnexo.ui.feriados.FeriadosViewModel
import com.example.csnexo.ui.oportunidades.form.OportunidadFormViewModel
import com.example.csnexo.ui.oportunidades.list.ListaOportunidadesViewModel
import com.example.csnexo.ui.oportunidades.resumen.ResumenViewModel
import com.example.csnexo.ui.organizaciones.form.OrganizacionFormViewModel
import com.example.csnexo.ui.organizaciones.list.OrganizacionListaViewModel
import com.example.csnexo.ui.eventos.form.EventoFormViewModel
import com.example.csnexo.ui.eventos.list.EventoListaViewModel
import com.example.csnexo.ui.contactos.form.ContactoFormViewModel
import com.example.csnexo.ui.contactos.list.ContactoListaViewModel
import com.example.csnexo.ui.seguimientos.form.SeguimientoFormViewModel
import com.example.csnexo.ui.seguimientos.list.SeguimientoListaViewModel
import com.example.csnexo.ui.alertas.AlertasViewModel

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
                repository = csnexoApplication().container.oportunidadRepository,
                servidor = csnexoApplication().container.oportunidadServidor
            )
        }
        initializer {
            OportunidadFormViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = csnexoApplication().container.oportunidadRepository,
                servidor = csnexoApplication().container.oportunidadServidor
            )
        }
        initializer {
            FeriadosViewModel(
                api = RetrofitClient.feriadosApi
            )
        }
        initializer {
            ResumenViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = csnexoApplication().container.oportunidadRepository
            )
        }
        initializer {
            OrganizacionListaViewModel(
                repository = csnexoApplication().container.organizacionRepository,
                servidor = csnexoApplication().container.organizacionServidor
            )
        }
        initializer {
            OrganizacionFormViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = csnexoApplication().container.organizacionRepository,
                servidor = csnexoApplication().container.organizacionServidor
            )
        }
        initializer {
            EventoListaViewModel(
                repository = csnexoApplication().container.eventoRepository,
                servidor = csnexoApplication().container.eventoServidor
            )
        }
        initializer {
            EventoFormViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = csnexoApplication().container.eventoRepository,
                servidor = csnexoApplication().container.eventoServidor
            )
        }
        initializer {
            ContactoListaViewModel(
                repository = csnexoApplication().container.contactoRepository,
                servidor = csnexoApplication().container.contactoServidor
            )
        }
        initializer {
            ContactoFormViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = csnexoApplication().container.contactoRepository,
                servidor = csnexoApplication().container.contactoServidor
            )
        }
        initializer {
            SeguimientoListaViewModel(
                repository = csnexoApplication().container.seguimientoRepository,
                servidor = csnexoApplication().container.seguimientoServidor
            )
        }
        initializer {
            SeguimientoFormViewModel(
                savedStateHandle = createSavedStateHandle(),
                repository = csnexoApplication().container.seguimientoRepository,
                servidor = csnexoApplication().container.seguimientoServidor
            )
        }
        initializer {
            AlertasViewModel(
                eventoRepository = csnexoApplication().container.eventoRepository,
                seguimientoRepository = csnexoApplication().container.seguimientoRepository
            )
        }
    }
}

private fun CreationExtras.csnexoApplication(): CSNexoApplication =
    this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as CSNexoApplication
