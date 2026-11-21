package com.example.csnexo

import com.example.csnexo.data.remote.FeriadoDto
import com.example.csnexo.data.remote.FeriadosApi
import com.example.csnexo.ui.feriados.FeriadosViewModel
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas del ViewModel de feriados (consumo de API externa).
 */
class FeriadosViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `carga exitosa llena la lista`() = runTest {
        val vm = FeriadosViewModel(
            FakeFeriadosApi(listOf(FeriadoDto("2026-01-01", "Año Nuevo", "New Year", "CL")))
        )

        assertTrue(!vm.uiState.value.cargando)
        assertEquals(1, vm.uiState.value.feriados.size)
        assertNull(vm.uiState.value.error)
    }

    @Test
    fun `fallo de red muestra error`() = runTest {
        val vm = FeriadosViewModel(FakeFeriadosApi(error = true))

        assertNotNull(vm.uiState.value.error)
        assertTrue(vm.uiState.value.feriados.isEmpty())
    }

    private class FakeFeriadosApi(
        private val resultado: List<FeriadoDto> = emptyList(),
        private val error: Boolean = false
    ) : FeriadosApi {
        override suspend fun feriados(anio: Int): List<FeriadoDto> {
            if (error) throw RuntimeException("sin red")
            return resultado
        }
    }
}
