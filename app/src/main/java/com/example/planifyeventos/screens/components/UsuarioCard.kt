package com.example.planifyeventos.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.planifyeventos.utils.formatarData

@Composable
fun UsuarioCard(
    nome: String,
    email: String,
    senha: String,
    data_nascimento: String,
    foto_perfil: String
) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFEAF6FF)),
        border = BorderStroke(2.dp, Color(0xFF008EFF)),
        elevation = CardDefaults.cardElevation(8.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Foto de Perfil
            Card(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                colors = CardDefaults.cardColors(containerColor = Color(0xFF037EF7))
            ) {
                AsyncImage(
                    model = foto_perfil,
                    contentDescription = "Foto de perfil",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )
                Text(
                    text = "URL da imagem: $foto_perfil",
                    color = Color.Red
                )

            }

            Spacer(modifier = Modifier.height(16.dp))

            // Nome
            Text(
                text = nome,
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF037EF7)
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = email,
                fontSize = 14.sp,
                color = Color.DarkGray
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.fillMaxWidth(0.8f))
            Spacer(modifier = Modifier.height(16.dp))

            // Data de nascimento
            Text(
                text = "Data de nascimento:",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = formatarData(data_nascimento),
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(16.dp))
            Divider(thickness = 1.dp, color = Color.Gray, modifier = Modifier.fillMaxWidth(0.8f))
            Spacer(modifier = Modifier.height(16.dp))

            // Senha
            Text(
                text = "Senha:",
                fontSize = 14.sp,
                color = Color.Gray
            )
            Text(
                text = senha,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )
        }
    }
}
