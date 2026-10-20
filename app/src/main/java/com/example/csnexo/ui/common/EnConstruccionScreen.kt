package com.example.csnexo.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.csnexo.ui.theme.CSNexoTheme

/**
 * Pantalla temporal para módulos que todavía no tienen su flujo vertical.
 * Se elimina o reemplaza a medida que avanza cada etapa.
 */
@Composable
fun EnConstruccionScreen(
    titulo: String,
    descripcion: String,
    modifier: Modifier = Modifier,
    onVolver: (() -> Unit)? = null
) {
    Scaffold(modifier = modifier.fillMaxSize()) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = titulo,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = descripcion,
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp)
            )
            if (onVolver != null) {
                OutlinedButton(
                    onClick = onVolver,
                    modifier = Modifier.padding(top = 24.dp)
                ) {
                    Text("Volver al menú")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun EnConstruccionScreenPreview() {
    CSNexoTheme {
        EnConstruccionScreen(
            titulo = "Oportunidades",
            descripcion = "Vista base de la Etapa 1."
        )
    }
}
