package com.example.csnexo

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.csnexo.domain.model.RolUsuario
import com.example.csnexo.domain.model.Usuario
import com.example.csnexo.ui.menu.MenuScreen
import com.example.csnexo.ui.theme.CSNexoTheme
import org.junit.Rule
import org.junit.Test

/**
 * Prueba de interfaz (instrumentada) del menú principal.
 * Verifica que se muestre el saludo del usuario y la tarjeta de Oportunidades.
 */
class MenuScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun el_menu_muestra_el_saludo_y_la_tarjeta_de_oportunidades() {
        composeRule.setContent {
            CSNexoTheme {
                MenuScreen(
                    usuario = Usuario(
                        id = "u1",
                        nombre = "Ejecutiva Demo",
                        usuario = "ejecutivo.demo",
                        rol = RolUsuario.EJECUTIVO
                    ),
                    onAbrirOportunidades = {},
                    onAbrirOrganizaciones = {},
                    onAbrirEventos = {},
                    onAbrirContactos = {},
                    onAbrirSeguimientos = {},
                    onAbrirAlertas = {},
                    onAbrirFeriados = {},
                    onCerrarSesion = {}
                )
            }
        }

        composeRule.onNodeWithText("Hola, Ejecutiva Demo").assertIsDisplayed()
        composeRule.onNodeWithText("Oportunidades").assertIsDisplayed()
    }
}
