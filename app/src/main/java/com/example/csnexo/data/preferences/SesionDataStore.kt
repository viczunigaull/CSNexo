package com.example.csnexo.data.preferences

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore

/**
 * Única instancia de DataStore para la sesión.
 *
 * Se declara como extensión de Context para que el contenedor de dependencias la
 * construya una sola vez por proceso.
 */
val Context.sesionDataStore: DataStore<Preferences> by preferencesDataStore(
    name = "sesion"
)

internal val CLAVE_USUARIO_SESION = stringPreferencesKey("usuario")
