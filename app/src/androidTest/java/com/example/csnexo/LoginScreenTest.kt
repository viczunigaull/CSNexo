package com.example.csnexo

import androidx.activity.ComponentActivity
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.example.csnexo.ui.auth.LoginScreen
import com.example.csnexo.ui.auth.LoginUiState
import com.example.csnexo.ui.theme.CSNexoTheme
import org.junit.Rule
import org.junit.Test

/**
 * Prueba de interfaz (instrumentada) de la pantalla de login.
 *
 * Vive en `src/androidTest/` porque necesita un emulador/dispositivo.
 * Se ejecuta con: `./gradlew connectedDebugAndroidTest`
 */
class LoginScreenTest {

    @get:Rule
    val composeRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    fun el_login_muestra_el_titulo_y_el_boton_ingresar() {
        composeRule.setContent {
            CSNexoTheme {
                LoginScreen(
                    state = LoginUiState(),
                    onUsuarioChange = {},
                    onClaveChange = {},
                    onIngresar = {}
                )
            }
        }

        composeRule.onNodeWithText("CSNexo").assertIsDisplayed()
        composeRule.onNodeWithText("Ingresar").assertIsDisplayed()
    }
}
