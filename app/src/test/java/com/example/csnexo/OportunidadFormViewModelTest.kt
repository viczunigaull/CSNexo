package com.example.csnexo

import androidx.lifecycle.SavedStateHandle
import com.example.csnexo.data.remote.OportunidadApi
import com.example.csnexo.data.remote.OportunidadDto
import com.example.csnexo.data.remote.OportunidadServidor
import com.example.csnexo.domain.model.Oportunidad
import com.example.csnexo.domain.model.PotencialComercial
import com.example.csnexo.domain.model.Prioridad
import com.example.csnexo.domain.model.TipoOportunidad
import com.example.csnexo.domain.repository.OportunidadRepository
import com.example.csnexo.ui.oportunidades.form.OportunidadFormViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

/**
 * Pruebas del ViewModel del formulario de oportunidades (validaciones y guardado).
 */
class OportunidadFormViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun `formulario vacio muestra errores y no guarda`() = runTest {
        val vm = crearViewModel()

        vm.guardar()

        val state = vm.uiState.value
        assertNotNull(state.nombreError)
        assertNotNull(state.tipoError)
        assertNotNull(state.descripcionError)
        assertTrue(!state.guardadoExitosamente)
    }

    @Test
    fun `formulario valido guarda correctamente`() = runTest {
        val vm = crearViewModel()

        vm.onNombreChange("Feria")
        vm.onTipoChange(TipoOportunidad.PATROCINIO)
        vm.onDescripcionChange("Descripcion")
        vm.onFuenteChange("Fuente")
        vm.onPrioridadChange(Prioridad.ALTA)
        vm.onPotencialChange(PotencialComercial.ALTO)
        vm.guardar()

        val state = vm.uiState.value
        assertNull(state.nombreError)
        assertTrue(state.guardadoExitosamente)
    }

    private fun crearViewModel(): OportunidadFormViewModel =
        OportunidadFormViewModel(
            savedStateHandle = SavedStateHandle(),
            repository = FakeRepo(),
            servidor = OportunidadServidor(FakeApi())
        )

    private class FakeRepo : OportunidadRepository {
        override fun observarTodas(): Flow<List<Oportunidad>> = flowOf(emptyList())

        override suspend fun obtenerPorId(id: Long): Oportunidad? = null

        override suspend fun crear(oportunidad: Oportunidad): Result<Long> = Result.success(1L)

        override suspend fun actualizar(oportunidad: Oportunidad): Result<Unit> = Result.success(Unit)

        override suspend fun eliminar(id: Long): Result<Unit> = Result.success(Unit)

        override suspend fun reemplazarTodas(oportunidades: List<Oportunidad>): Result<Unit> =
            Result.success(Unit)
    }

    private class FakeApi : OportunidadApi {
        override suspend fun listar(): List<OportunidadDto> = emptyList()

        override suspend fun obtener(id: Long): OportunidadDto = OportunidadDto(
            id = id,
            nombre = "x",
            tipo = "OTRO",
            descripcion = "d",
            fuenteInformacion = "f",
            prioridad = "MEDIA",
            potencialComercial = "MEDIO",
            estadoGestion = "EVENTO_IDENTIFICADO"
        )

        override suspend fun crear(oportunidad: OportunidadDto): OportunidadDto =
            oportunidad.copy(id = 1)

        override suspend fun actualizar(id: Long, oportunidad: OportunidadDto): OportunidadDto =
            oportunidad

        override suspend fun eliminar(id: Long) = Unit
    }
}
