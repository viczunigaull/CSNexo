package com.example.csnexo

import com.example.csnexo.domain.model.RolUsuario
import com.example.csnexo.domain.model.Usuario
import com.example.csnexo.domain.repository.CredencialesInvalidasException
import com.example.csnexo.domain.repository.SesionRepository
import com.example.csnexo.ui.auth.LoginUiState
import com.example.csnexo.ui.auth.LoginViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas del ViewModel de login.
 */
class LoginViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `campos vacios muestran errores y no inician sesion`() = runTest {
        val vm = LoginViewModel(FakeSesionRepository(Result.success(usuario())))

        vm.iniciarSesion()

        val state = vm.uiState.value
        assertNotNull(state.usuarioError)
        assertNotNull(state.claveError)
    }

    @Test
    fun `credenciales invalidas muestran error`() = runTest {
        val vm = LoginViewModel(
            FakeSesionRepository(Result.failure(CredencialesInvalidasException()))
        )

        vm.onUsuarioChange("x")
        vm.onClaveChange("y")
        vm.iniciarSesion()

        assertNotNull(vm.uiState.value.error)
    }

    @Test
    fun `login exitoso limpia el estado`() = runTest {
        val vm = LoginViewModel(FakeSesionRepository(Result.success(usuario())))

        vm.onUsuarioChange("ejecutivo.demo")
        vm.onClaveChange("demo1234")
        vm.iniciarSesion()

        assertEquals(LoginUiState(), vm.uiState.value)
    }

    private fun usuario() = Usuario(
        id = "u1",
        nombre = "Demo",
        usuario = "ejecutivo.demo",
        rol = RolUsuario.EJECUTIVO
    )

    private class FakeSesionRepository(
        private val resultado: Result<Usuario>
    ) : SesionRepository {
        override val sesion: Flow<Usuario?> = flowOf(null)

        override suspend fun iniciarSesion(usuario: String, clave: String): Result<Usuario> = resultado

        override suspend fun cerrarSesion() = Unit
    }
}
