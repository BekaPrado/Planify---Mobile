package com.example.planifyeventos.screens.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.toggleable
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.graphics.Shape
import androidx.compose.foundation.shape.RoundedCornerShape

@Composable
fun MultiSelectDialog(
    titulo: String,
    opcoes: List<String>,
    selecionados: List<String>,
    onConfirmar: (List<String>) -> Unit,
    onDismiss: () -> Unit
) {
    val selecionadosState = remember { mutableStateListOf<String>().apply { addAll(selecionados) } }
    val fundoAzul = Color(0xFF0025DA)
    val branco = Color.White
    val shape: Shape = RoundedCornerShape(16.dp)

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = shape,
            color = fundoAzul,
            tonalElevation = 8.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(20.dp)
            ) {
                Text(
                    text = titulo,
                    color = branco,
                    style = MaterialTheme.typography.titleLarge.copy(fontSize = 20.sp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                opcoes.forEach { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 6.dp)
                            .toggleable(
                                value = item in selecionadosState,
                                onValueChange = {
                                    if (it) selecionadosState.add(item)
                                    else selecionadosState.remove(item)
                                }
                            ),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = item in selecionadosState,
                            onCheckedChange = null,
                            colors = CheckboxDefaults.colors(
                                checkedColor = branco,
                                uncheckedColor = branco
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = item,
                            color = branco,
                            style = MaterialTheme.typography.bodyLarge
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = branco)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    TextButton(onClick = { onConfirmar(selecionadosState.toList()) }) {
                        Text("Filtrar", color = branco)
                    }
                }
            }
        }
    }
}
