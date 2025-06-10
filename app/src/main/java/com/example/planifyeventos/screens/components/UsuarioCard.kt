package com.example.planifyeventos.screens.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import com.example.planifyeventos.utils.formatarData
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun UsuarioCard(
    nome: String = "Nome de usuário",
    email: String = "Email de usuário",
    senha: String = "Senha",
    data_nascimento: String = "Data de nascimento",
    palavra_chave: String = "Palavra chave",
    foto_perfil: String = "Url da imagem"
) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .height(600.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xaaffffff)),
        border = BorderStroke(
            width = 16.dp,
            color = Color(0xFF008EFF)
        )
    ) {
        Column (
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Card(
                modifier = Modifier
                    .padding(top = 26.dp)
                    .size(90.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF037EF7)),
                shape = CircleShape
            ) {
                AsyncImage(
                    model = foto_perfil,
                    contentDescription = "",
                    modifier = Modifier
                        .fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            }
            Spacer(modifier = Modifier.height(36.dp))
            Column {
                Text(text = "Nome de usuário:")
                Text(text = nome)
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.width(300.dp), color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Meu email:")
                Text(text = email)
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.width(300.dp), color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Data de nascimento:")
                Text(text = data_nascimento.formatarData())
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.width(300.dp), color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Minha palavra chave:")
                Text(text = palavra_chave)
                Spacer(modifier = Modifier.height(8.dp))
                HorizontalDivider(modifier = Modifier.width(300.dp), color = Color.Black)
                Spacer(modifier = Modifier.height(8.dp))
                Text(text = "Minha senha:")
                Text(text = senha)
            }
        }
    }
}

@Preview
@Composable
private fun UsurioCardPreview() {
    UsuarioCard()
}