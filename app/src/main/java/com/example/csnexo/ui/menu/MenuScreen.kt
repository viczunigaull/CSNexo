package com.example.csnexo.ui.menu

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.csnexo.domain.model.RolUsuario
import com.example.csnexo.domain.model.Usuario
import com.example.csnexo.ui.theme.CSNexoTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MenuScreen(
    usuario: Usuario,
    onAbrirOportunidades: () -> Unit,
    onCerrarSesion: () -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("CSNexo") },
                actions = {
                    TextButton(onClick = onCerrarSesion) {
                        Text("Salir")
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 24.dp)
        ) {
            Text(
                text = "Hola, ${usuario.nombre}",
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = "${usuario.rol.etiqueta()} · ${usuario.usuario}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(top = 4.dp)
            )

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = "Módulos",
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(12.dp))

            TarjetaModulo(
                titulo = "Oportunidades",
                descripcion = "Crear, editar y revisar las oportunidades registradas.",
                habilitado = true,
                onClick = onAbrirOportunidades
            )

            Spacer(modifier = Modifier.height(12.dp))

            TarjetaModulo(
                titulo = "Eventos",
                descripcion = "Disponible en el alcance transversal.",
                habilitado = false,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(12.dp))

            TarjetaModulo(
                titulo = "Seguimiento",
                descripcion = "Disponible en el alcance transversal.",
                habilitado = false,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(32.dp))

            OutlinedButton(
                onClick = onCerrarSesion,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Cerrar sesión")
            }
        }
    }
}

@Composable
private fun TarjetaModulo(
    titulo: String,
    descripcion: String,
    habilitado: Boolean,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (habilitado) {
                MaterialTheme.colorScheme.surface
            } else {
                MaterialTheme.colorScheme.surfaceVariant
            }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = habilitado, onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = titulo,
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = descripcion,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Text(
                text = if (habilitado) "Abrir" else "Próximamente",
                style = MaterialTheme.typography.labelMedium,
                color = if (habilitado) {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                modifier = Modifier.padding(start = 12.dp)
            )
        }
    }
}

private fun RolUsuario.etiqueta(): String = when (this) {
    RolUsuario.EJECUTIVO -> "Ejecutivo comercial"
    RolUsuario.SUPERVISOR -> "Supervisor"
    RolUsuario.MARKETING -> "Marketing"
}

@Preview(showBackground = true)
@Composable
private fun MenuScreenPreview() {
    CSNexoTheme {
        MenuScreen(
            usuario = Usuario(
                id = "u-ejecutivo",
                nombre = "Ejecutiva Demo",
                usuario = "ejecutivo.demo",
                rol = RolUsuario.EJECUTIVO
            ),
            onAbrirOportunidades = {},
            onCerrarSesion = {}
        )
    }
}
