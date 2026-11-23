package com.example.csnexo

import androidx.lifecycle.SavedStateHandle
import com.example.csnexo.data.remote.OportunidadServidor
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import com.example.csnexo.domain.repository.OportunidadRepository
import com.example.csnexo.ui.oportunidades.form.OportunidadFormViewModel
import io.kotest.core.spec.style.StringSpec
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain

@OptIn(ExperimentalCoroutinesApi::class)
class OportunidadFormViewModelTest : StringSpec({

    val dispatcher = StandardTestDispatcher()

    beforeSpec { Dispatchers.setMain(dispatcher) }
    afterSpec { Dispatchers.resetMain() }

    "formulario vacio muestra errores y no guarda" {
        runTest(dispatcher) {
            val repo = mockk<OportunidadRepository>()
            val servidor = mockk<OportunidadServidor>(relaxed = true)
            val vm = OportunidadFormViewModel(SavedStateHandle(), repo, servidor)

            vm.guardar()
            advanceUntilIdle()

            vm.uiState.value.nombreError.shouldNotBeNull()
            vm.uiState.value.tipoError.shouldNotBeNull()
            vm.uiState.value.guardadoExitosamente shouldBe false
        }
    }

    "formulario valido guarda correctamente" {
        runTest(dispatcher) {
            val repo = mockk<OportunidadRepository>()
            coEvery { repo.crear(any()) } returns Result.success(1L)
            val servidor = mockk<OportunidadServidor>(relaxed = true)
            val vm = OportunidadFormViewModel(SavedStateHandle(), repo, servidor)

            vm.onNombreChange("Feria")
            vm.onTipoChange(TipoOportunidad.PATROCINIO)
            vm.onDescripcionChange("Descripcion")
            vm.onFuenteChange("Fuente")
            vm.onPrioridadChange(Prioridad.ALTA)
            vm.onPotencialChange(PotencialComercial.ALTO)
            vm.guardar()
            advanceUntilIdle()

            vm.uiState.value.guardadoExitosamente shouldBe true
        }
    }
})
