package com.example.csnexo.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.csnexo.domain.model.Usuario
import com.example.csnexo.ui.menu.MenuScreen
import com.example.csnexo.ui.oportunidades.form.OportunidadFormRoute
import com.example.csnexo.ui.oportunidades.list.ListaOportunidadesRoute

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
                onGuardado = { navController.popBackStack() }
            )
        }
    }
}
