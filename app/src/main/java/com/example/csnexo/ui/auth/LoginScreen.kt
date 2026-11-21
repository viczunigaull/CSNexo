package com.example.csnexo.ui.auth

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.csnexo.di.AppViewModelProvider
import com.example.csnexo.ui.components.CampoTexto
import com.example.csnexo.ui.components.MensajeError
import com.example.csnexo.ui.theme.CSNexoTheme

@Composable
fun LoginRoute(
    viewModel: LoginViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()

    LoginScreen(
        state = state,
        onUsuarioChange = viewModel::onUsuarioChange,
        onClaveChange = viewModel::onClaveChange,
        onIngresar = viewModel::iniciarSesion
    )
}

@Composable
fun LoginScreen(
    state: LoginUiState,
    onUsuarioChange: (String) -> Unit,
    onClaveChange: (String) -> Unit,
    onIngresar: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "CSNexo",
                style = MaterialTheme.typography.displaySmall,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = "Gestión móvil de oportunidades",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            Spacer(modifier = Modifier.height(32.dp))

            CampoTexto(
                label = "Usuario",
                valor = state.usuario,
                onCambio = onUsuarioChange,
                error = state.usuarioError,
                icono = Icons.Default.Person,
                enabled = !state.cargando
            )

            Spacer(modifier = Modifier.height(12.dp))

            CampoTexto(
                label = "Clave",
                valor = state.clave,
                onCambio = onClaveChange,
                error = state.claveError,
                icono = Icons.Default.Lock,
                contrasena = true,
                enabled = !state.cargando
            )

            Spacer(modifier = Modifier.height(12.dp))

            MensajeError(state.error)

            Spacer(modifier = Modifier.height(20.dp))

            Button(
                onClick = onIngresar,
                enabled = !state.cargando,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
            ) {
                if (state.cargando) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp,
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text("Ingresar")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Card(
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Usuarios de prueba",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "ejecutivo.demo / demo1234",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                    Text(
                        text = "supervisor.demo / demo1234",
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = "Datos ficticios solo para la evaluación.",
                        style = MaterialTheme.typography.labelSmall,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun LoginScreenPreview() {
    CSNexoTheme {
        LoginScreen(
            state = LoginUiState(usuario = "ejecutivo.demo"),
            onUsuarioChange = {},
            onClaveChange = {},
            onIngresar = {}
        )
    }
}
