package com.example.csnexo

import com.example.csnexo.domain.model.RolUsuario
import com.example.csnexo.domain.model.Usuario
import com.example.csnexo.domain.repository.CredencialesInvalidasException
import com.example.csnexo.domain.repository.SesionRepository
import com.example.csnexo.ui.auth.LoginUiState
import com.example.csnexo.ui.auth.LoginViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class LoginViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeSpec { Dispatchers.setMain(dispatcher) }
    afterSpec { Dispatchers.resetMain() }

    "campos vacios muestran errores y no inician sesion" {
        runTest(dispatcher) {
            val repo = mockk<SesionRepository>()
            coEvery { repo.iniciarSesion(any(), any()) } returns Result.success(usuario())
            val vm = LoginViewModel(repo)

            vm.iniciarSesion()
            advanceUntilIdle()

            vm.uiState.value.usuarioError.shouldNotBeNull()
            vm.uiState.value.claveError.shouldNotBeNull()
        }
    }

    "credenciales invalidas muestran error" {
        runTest(dispatcher) {
            val repo = mockk<SesionRepository>()
            coEvery { repo.iniciarSesion(any(), any()) } returns
                Result.failure(CredencialesInvalidasException())
            val vm = LoginViewModel(repo)

            vm.onUsuarioChange("x")
            vm.onClaveChange("y")
            vm.iniciarSesion()
            advanceUntilIdle()

            vm.uiState.value.error.shouldNotBeNull()
        }
    }

    "login exitoso limpia el estado" {
        runTest(dispatcher) {
            val repo = mockk<SesionRepository>()
            coEvery { repo.iniciarSesion(any(), any()) } returns Result.success(usuario())
            val vm = LoginViewModel(repo)

            vm.onUsuarioChange("ejecutivo.demo")
            vm.onClaveChange("demo1234")
            vm.iniciarSesion()
            advanceUntilIdle()

            vm.uiState.value shouldBe LoginUiState()
        }
    }
}) {
    companion object {
        fun usuario() = Usuario(
            id = "u1",
            nombre = "Demo",
            usuario = "ejecutivo.demo",
            rol = RolUsuario.EJECUTIVO
        )
    }
}
