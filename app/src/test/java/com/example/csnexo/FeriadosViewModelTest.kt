package com.example.csnexo

import com.example.csnexo.data.remote.FeriadoDto
import com.example.csnexo.data.remote.FeriadosApi
import com.example.csnexo.ui.feriados.FeriadosViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.collections.shouldContainExactly
import io.kotest.matchers.nulls.shouldBeNull
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
class FeriadosViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeSpec { Dispatchers.setMain(dispatcher) }
    afterSpec { Dispatchers.resetMain() }

    "carga exitosa llena la lista" {
        runTest(dispatcher) {
            val esperado = listOf(FeriadoDto("2026-01-01", "Año Nuevo", "New Year", "CL"))
            val api = mockk<FeriadosApi>()
            coEvery { api.feriados(any()) } returns esperado

            val vm = FeriadosViewModel(api)
            advanceUntilIdle()

            vm.uiState.value.cargando shouldBe false
            vm.uiState.value.feriados shouldContainExactly esperado
            vm.uiState.value.error.shouldBeNull()
        }
    }

    "fallo de red muestra error" {
        runTest(dispatcher) {
            val api = mockk<FeriadosApi>()
            coEvery { api.feriados(any()) } throws RuntimeException("sin red")

            val vm = FeriadosViewModel(api)
            advanceUntilIdle()

            vm.uiState.value.error.shouldNotBeNull()
        }
    }
})
