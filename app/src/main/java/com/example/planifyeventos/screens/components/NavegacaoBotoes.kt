package com.example.planifyeventos.screens.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planifyeventos.R

@Composable
fun NavegacaoBotoes(navegacao: NavHostController) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CustomBotaoNavegacao(
            imagemId = R.drawable.logo,
            texto = "Eventos",
            onClick = { navegacao.navigate("eventos_criados") }
        )

        CustomBotaoNavegacao(
            imagemId = R.drawable.logo,
            texto = "Ingressos",
            onClick = { navegacao.navigate("ingressos") }
        )

        CustomBotaoNavegacao(
            imagemId = R.drawable.logo,
            texto = "Perfil",
            onClick = { navegacao.navigate("perfil") }
        )
    }
}

@Composable
fun CustomBotaoNavegacao(
    imagemId: Int,
    texto: String,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .width(90.dp)
            .height(90.dp),
        contentPadding = PaddingValues(4.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF1565C0), // Azul escuro
            contentColor = Color.White         // Texto branco
        )
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Image(
                painter = painterResource(id = imagemId),
                contentDescription = texto,
                modifier = Modifier.size(32.dp)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = texto, fontSize = 12.sp)
        }
    }
}
