package com.example.planifyeventos.screens
import com.example.planifyeventos.service.RetrofitEmailFactory

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planifyeventos.R
import com.example.planifyeventos.model.EmailRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.net.Uri
import android.util.Log

@Composable
fun RecuperarSenha(navegacao: NavHostController?) {
    val email = remember { mutableStateOf(TextFieldValue("")) }
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
                .border(15.dp, Color(0xFF037EF7), shape = RoundedCornerShape(40.dp))
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.logo),
                contentDescription = "Logo",
                modifier = Modifier
                    .height(90.dp)
                    .width(230.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recuperar senha",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF037EF7)
            )

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = email.value,
                onValueChange = { email.value = it },
                label = { Text("Digite seu e-mail") },
                shape = RoundedCornerShape(30.dp),
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .shadow(4.dp, RoundedCornerShape(30.dp))
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (email.value.text.isBlank()) {
                        mensagem.value = "Preencha o e-mail"
                        return@Button
                    }

                    loading.value = true
                    mensagem.value = null

                    val requisicao = EmailRequest(
                        email = email.value.text
                    )


                    RetrofitEmailFactory.getEmailService().enviarEmail(requisicao)
                        .enqueue(object : Callback<Void> {
                            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                loading.value = false
                                if (response.isSuccessful) {
                                    val emailEncoded = Uri.encode(email.value.text)
                                    navegacao?.navigate("verificar_codigo/$emailEncoded")
                                } else {
                                    Log.e("EmailErro", "Erro HTTP ${response.code()}")
                                    mensagem.value = "Erro ao enviar e-mail. Código: ${response.code()}"
                                }
                            }

                            override fun onFailure(call: Call<Void>, t: Throwable) {
                                loading.value = false
                                Log.e("EmailErro", "Erro de rede", t)
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
                Text(
                    text = if (loading.value) "Enviando..." else "Enviar Código",
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }

            mensagem.value?.let {
                Spacer(modifier = Modifier.height(16.dp))
                Text(it, color = if (it.contains("Erro")) Color.Red else Color(0xFF037EF7))
            }
        }
    }
}

