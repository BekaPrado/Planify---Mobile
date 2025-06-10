package com.example.planifyeventos.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planifyeventos.model.ValidacaoResponse
import com.example.planifyeventos.service.RetrofitEmailFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun VerificarCodigo(navegacao: NavHostController, email: String) {
    val codigo = remember { mutableStateOf("") }
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
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Digite o código enviado para:\n$email",
                fontSize = 18.sp,
                lineHeight = 22.sp,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = codigo.value,
                onValueChange = { codigo.value = it },
                label = { Text("Código") },
                singleLine = true,
                shape = RoundedCornerShape(30.dp),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (codigo.value.isBlank()) {
                        mensagem.value = "Informe o código"
                        return@Button
                    }

                    loading.value = true
                    val request = mapOf("email" to email, "codigo" to codigo.value)

                    RetrofitEmailFactory.getEmailService().validarCodigo(request)
                        .enqueue(object : Callback<ValidacaoResponse> {
                            override fun onResponse(call: Call<ValidacaoResponse>, response: Response<ValidacaoResponse>) {
                                loading.value = false
                                if (response.isSuccessful && response.body() != null) {
                                    val idUsuario = response.body()!!.id_usuario
                                    navegacao.navigate("redefinir_senha/$idUsuario")
                                } else {
                                    mensagem.value = "Código incorreto ou expirado."
                                }
                            }

                            override fun onFailure(call: Call<ValidacaoResponse>, t: Throwable) {
                                loading.value = false
                                mensagem.value = "Erro de rede: ${t.message}"
                            }
                        })
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                shape = RoundedCornerShape(30.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF037EF7))
            ) {
                Text(if (loading.value) "Verificando..." else "Verificar", fontSize = 16.sp)
            }

            mensagem.value?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = Color.Red)
            }
        }
    }
}
