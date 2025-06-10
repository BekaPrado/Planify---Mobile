package com.example.planifyeventos.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planifyeventos.R
import com.example.planifyeventos.service.RetrofitFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun RedefinirSenhaScreen(navegacao: NavHostController?, idUsuario: Int) {
    var novaSenha by remember { mutableStateOf("") }
    var confirmarSenha by remember { mutableStateOf("") }
    val mensagem = remember { mutableStateOf<String?>(null) }
    val loading = remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier
                .padding(24.dp)
                .fillMaxWidth()
                .background(Color.White, shape = RoundedCornerShape(40.dp))
                .border(15.dp, Color(0xFF007AFF), shape = RoundedCornerShape(40.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(id = R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier.size(100.dp),
                contentScale = ContentScale.Fit
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Redefinir Senha:",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF007AFF),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            Text("Nova Senha:", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = novaSenha,
                onValueChange = { novaSenha = it },
                placeholder = { Text("Digite sua nova senha") },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(30.dp))
                    .background(Color(0xFFF2F9FF), RoundedCornerShape(30.dp))
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text("Confirmar Senha:", fontSize = 16.sp)
            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = confirmarSenha,
                onValueChange = { confirmarSenha = it },
                placeholder = { Text("Confirme sua senha") },
                visualTransformation = PasswordVisualTransformation(),
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(10.dp, RoundedCornerShape(30.dp))
                    .background(Color(0xFFF2F9FF), RoundedCornerShape(30.dp))
            )

            Spacer(modifier = Modifier.height(40.dp))

            Button(
                onClick = {
                    if (novaSenha != confirmarSenha) {
                        mensagem.value = "As senhas não correspondem."
                        return@Button
                    }

                    if (novaSenha.isBlank()) {
                        mensagem.value = "Por favor, insira uma nova senha."
                        return@Button
                    }

                    loading.value = true
                    mensagem.value = null

                    val usuarioService = RetrofitFactory().getUsuarioService()

                    val json = JSONObject().apply {
                        put("senha", novaSenha)
                    }

                    val body = json.toString()
                        .toRequestBody("application/json".toMediaType())

                    usuarioService.redefinirSenhaRaw(idUsuario, body)
                        .enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                loading.value = false
                                if (response.isSuccessful) {
                                    mensagem.value = "Senha redefinida com sucesso!"
                                    navegacao?.navigate("login")
                                } else {
                                    mensagem.value = "Erro ao redefinir senha. Código: ${response.code()}"
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                loading.value = false
                                mensagem.value = "Erro de rede: ${t.message}"
                            }
                        })
                },
                shape = RoundedCornerShape(50.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(8.dp, RoundedCornerShape(50.dp)),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF007AFF),
                    contentColor = Color.White
                )
            ) {
                Text(
                    text = if (loading.value) "Carregando..." else "Alterar Senha",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            mensagem.value?.let {
                Spacer(modifier = Modifier.height(20.dp))
                Text(
                    text = it,
                    color = if (it.startsWith("Senha")) Color(0xFF037EF7) else Color.Red,
                    fontWeight = FontWeight.Medium,
                    fontSize = 16.sp
                )
            }
        }
    }
}
