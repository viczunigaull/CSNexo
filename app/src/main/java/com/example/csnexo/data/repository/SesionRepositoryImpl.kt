package com.example.csnexo.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import com.example.csnexo.data.preferences.CLAVE_USUARIO_SESION
import com.example.csnexo.domain.model.Usuario
import com.example.csnexo.domain.repository.AuthRepository
import com.example.csnexo.domain.repository.SesionRepository
import java.io.IOException
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

/**
 * Implementación local de la sesión.
 *
 * Solo persiste el nombre de usuario; el perfil se vuelve a resolver contra el
 * repositorio de autenticación. Así no se guardan contraseñas en DataStore.
 */
class SesionRepositoryImpl(
    private val dataStore: DataStore<Preferences>,
    private val authRepository: AuthRepository
) : SesionRepository {

    override val sesion: Flow<Usuario?> = dataStore.data
        // Un error de lectura de DataStore se trata como "sin sesión".
        .catch { error ->
            if (error is IOException) {
                emit(emptyPreferences())
            } else {
                throw error
            }
        }
        .map { preferences ->
            val usuario = preferences[CLAVE_USUARIO_SESION] ?: return@map null
            authRepository.buscarPorUsuario(usuario)
        }

    override suspend fun iniciarSesion(usuario: String, clave: String): Result<Usuario> {
        val resultado = authRepository.autenticar(usuario, clave)
        val autenticado = resultado.getOrNull() ?: return resultado

        return try {
            dataStore.edit { preferences ->
                preferences[CLAVE_USUARIO_SESION] = autenticado.usuario
            }
            Result.success(autenticado)
        } catch (error: CancellationException) {
            throw error
        } catch (error: Exception) {
            Result.failure(error)
        }
    }

    override suspend fun cerrarSesion() {
        dataStore.edit { preferences ->
            preferences.remove(CLAVE_USUARIO_SESION)
        }
    }
}
