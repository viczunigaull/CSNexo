package com.example.csnexo.ui.oportunidades.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.csnexo.domain.repository.OportunidadRepository
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update

@OptIn(ExperimentalCoroutinesApi::class)
class ListaOportunidadesViewModel(
    private val repository: OportunidadRepository
) : ViewModel() {

    private val reintentos = MutableStateFlow(0)

    val uiState: StateFlow<ListaOportunidadesUiState> = reintentos
        .flatMapLatest {
            repository.observarTodas()
                .map { oportunidades ->
                    ListaOportunidadesUiState(
                        cargando = false,
                        oportunidades = oportunidades
                    )
                }
                .onStart {
                    emit(ListaOportunidadesUiState(cargando = true))
                }
                .catch { error ->
                    if (error is CancellationException) throw error
                    emit(
                        ListaOportunidadesUiState(
                            cargando = false,
                            error = "No se pudieron cargar las oportunidades."
                        )
                    )
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = ListaOportunidadesUiState()
        )

    fun reintentar() {
        reintentos.update { it + 1 }
    }
}
