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
                onAbrirFeriados = {
                    navController.navigate(Routes.FERIADOS)
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
    }
}
