package com.example.csnexo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.csnexo.domain.model.Usuario
import com.example.csnexo.ui.feriados.FeriadosRoute
import com.example.csnexo.ui.menu.MenuScreen
import com.example.csnexo.ui.oportunidades.form.OportunidadFormRoute
import com.example.csnexo.ui.oportunidades.list.ListaOportunidadesRoute
import com.example.csnexo.ui.oportunidades.resumen.ResumenRoute
import com.example.csnexo.ui.organizaciones.form.OrganizacionFormRoute
import com.example.csnexo.ui.organizaciones.list.OrganizacionListaRoute
import com.example.csnexo.ui.eventos.form.EventoFormRoute
import com.example.csnexo.ui.eventos.list.EventoListaRoute
import com.example.csnexo.ui.contactos.form.ContactoFormRoute
import com.example.csnexo.ui.contactos.list.ContactoListaRoute
import com.example.csnexo.ui.seguimientos.form.SeguimientoFormRoute
import com.example.csnexo.ui.seguimientos.list.SeguimientoListaRoute
import com.example.csnexo.ui.alertas.AlertasRoute
import com.example.csnexo.ui.activaciones.form.ActivacionFormRoute
import com.example.csnexo.ui.activaciones.list.ActivacionListaRoute
import com.example.csnexo.ui.resultados.form.ResultadoFormRoute
import com.example.csnexo.ui.resultados.list.ResultadoListaRoute

/**
 * Grafo de navegación autenticado.
 *
 * La lista y el formulario comparten el repositorio de oportunidades. La ruta de
 * edición recibe el ID como argumento Long y el ViewModel lo recupera desde
 * SavedStateHandle.
 */
@Composable
fun CSNexoNavHost(
    usuario: Usuario,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.MENU,
        modifier = modifier
    ) {
        composable(Routes.MENU) {
            MenuScreen(
                usuario = usuario,
                onAbrirOportunidades = {
                    navController.navigate(Routes.OPORTUNIDADES)
                },
                onAbrirOrganizaciones = {
                    navController.navigate(Routes.ORGANIZACIONES)
                },
                onAbrirEventos = {
                    navController.navigate(Routes.EVENTOS)
                },
                onAbrirContactos = {
                    navController.navigate(Routes.CONTACTOS)
                },
                onAbrirSeguimientos = {
                    navController.navigate(Routes.SEGUIMIENTOS)
                },
                onAbrirAlertas = {
                    navController.navigate(Routes.ALERTAS)
                },
                onAbrirFeriados = {
                    navController.navigate(Routes.FERIADOS)
                },
                onAbrirActivaciones = {
                    navController.navigate(Routes.ACTIVACIONES)
                },
                onAbrirResultados = {
                    navController.navigate(Routes.RESULTADOS)
                },
                onCerrarSesion = onCerrarSesion
            )
        }

        composable(Routes.OPORTUNIDADES) {
            ListaOportunidadesRoute(
                onVolver = { navController.popBackStack() },
                onAgregar = {
                    navController.navigate(Routes.OPORTUNIDAD_NUEVA)
                },
                onEditar = { oportunidadId ->
                    navController.navigate(Routes.editar(oportunidadId))
                }
            )
        }

        composable(Routes.OPORTUNIDAD_NUEVA) {
            OportunidadFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { id -> navController.navigate(Routes.resumen(id)) }
            )
        }

        composable(Routes.FERIADOS) {
            FeriadosRoute(
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Routes.ALERTAS) {
            AlertasRoute(
                onVolver = { navController.popBackStack() }
            )
        }

        composable(Routes.ACTIVACIONES) {
            ActivacionListaRoute(
                onVolver = { navController.popBackStack() },
                onAgregar = { navController.navigate(Routes.ACTIVACION_NUEVA) },
                onEditar = { id -> navController.navigate(Routes.activacionEditar(id)) }
            )
        }

        composable(Routes.ACTIVACION_NUEVA) {
            ActivacionFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ACTIVACION_EDITAR,
            arguments = listOf(
                navArgument(Routes.ARG_ACTIVACION_ID) {
                    type = NavType.LongType
                }
            )
        ) {
            ActivacionFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(Routes.RESULTADOS) {
            ResultadoListaRoute(
                onVolver = { navController.popBackStack() },
                onAgregar = { navController.navigate(Routes.RESULTADO_NUEVA) },
                onEditar = { id -> navController.navigate(Routes.resultadoEditar(id)) }
            )
        }

        composable(Routes.RESULTADO_NUEVA) {
            ResultadoFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.RESULTADO_EDITAR,
            arguments = listOf(
                navArgument(Routes.ARG_RESULTADO_ID) {
                    type = NavType.LongType
                }
            )
        ) {
            ResultadoFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.OPORTUNIDAD_EDITAR,
            arguments = listOf(
                navArgument(Routes.ARG_OPORTUNIDAD_ID) {
                    type = NavType.LongType
                }
            )
        ) {
            OportunidadFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { id -> navController.navigate(Routes.resumen(id)) }
            )
        }

        composable(
            route = Routes.RESUMEN,
            arguments = listOf(
                navArgument(Routes.ARG_OPORTUNIDAD_ID) {
                    type = NavType.LongType
                }
            )
        ) {
            ResumenRoute(
                onVolver = { navController.popBackStack(Routes.OPORTUNIDADES, false) },
                onEditar = { id -> navController.navigate(Routes.editar(id)) }
            )
        }

        composable(Routes.ORGANIZACIONES) {
            OrganizacionListaRoute(
                onVolver = { navController.popBackStack() },
                onAgregar = { navController.navigate(Routes.ORGANIZACION_NUEVA) },
                onEditar = { id -> navController.navigate(Routes.organizacionEditar(id)) }
            )
        }

        composable(Routes.ORGANIZACION_NUEVA) {
            OrganizacionFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.ORGANIZACION_EDITAR,
            arguments = listOf(
                navArgument(Routes.ARG_ORGANIZACION_ID) {
                    type = NavType.LongType
                }
            )
        ) {
            OrganizacionFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(Routes.EVENTOS) {
            EventoListaRoute(
                onVolver = { navController.popBackStack() },
                onAgregar = { navController.navigate(Routes.EVENTO_NUEVA) },
                onEditar = { id -> navController.navigate(Routes.eventoEditar(id)) }
            )
        }

        composable(Routes.EVENTO_NUEVA) {
            EventoFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.EVENTO_EDITAR,
            arguments = listOf(
                navArgument(Routes.ARG_EVENTO_ID) {
                    type = NavType.LongType
                }
            )
        ) {
            EventoFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(Routes.CONTACTOS) {
            ContactoListaRoute(
                onVolver = { navController.popBackStack() },
                onAgregar = { navController.navigate(Routes.CONTACTO_NUEVA) },
                onEditar = { id -> navController.navigate(Routes.contactoEditar(id)) }
            )
        }

        composable(Routes.CONTACTO_NUEVA) {
            ContactoFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.CONTACTO_EDITAR,
            arguments = listOf(
                navArgument(Routes.ARG_CONTACTO_ID) {
                    type = NavType.LongType
                }
            )
        ) {
            ContactoFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(Routes.SEGUIMIENTOS) {
            SeguimientoListaRoute(
                onVolver = { navController.popBackStack() },
                onAgregar = { navController.navigate(Routes.SEGUIMIENTO_NUEVA) },
                onEditar = { id -> navController.navigate(Routes.seguimientoEditar(id)) }
            )
        }

        composable(Routes.SEGUIMIENTO_NUEVA) {
            SeguimientoFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }

        composable(
            route = Routes.SEGUIMIENTO_EDITAR,
            arguments = listOf(
                navArgument(Routes.ARG_SEGUIMIENTO_ID) {
                    type = NavType.LongType
                }
            )
        ) {
            SeguimientoFormRoute(
                onVolver = { navController.popBackStack() },
                onGuardado = { navController.popBackStack() }
            )
        }
    }
}
