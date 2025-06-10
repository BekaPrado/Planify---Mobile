package com.example.planifyeventos.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planifyeventos.R
import com.example.planifyeventos.service.RetrofitFactory
import com.example.planifyeventos.utils.SharedPrefHelper
import retrofit2.Callback
import com.example.planifyeventos.model.Result
import retrofit2.Call
import retrofit2.Response



@Composable
fun Login(navegacao: NavHostController?) {
    val email = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }
    val context = LocalContext.current
    val erro = remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(color = Color.White)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Card(
                modifier = Modifier.fillMaxSize(),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(30.dp),
                border = BorderStroke(14.dp, Color(0xFF037EF7))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(40.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Image(
                        painter = painterResource(R.drawable.logo),
                        contentDescription = "",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp)
                    )
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(text = "Email:", fontSize = 15.sp)
                        OutlinedTextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            shape = RoundedCornerShape(33.dp),
                            singleLine = true,
                            modifier = Modifier.height(55.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "Senha:", fontSize = 15.sp)
                        OutlinedTextField(
                            value = senha.value,
                            onValueChange = { senha.value = it },
                            shape = RoundedCornerShape(33.dp),
                            singleLine = true,
                            modifier = Modifier.height(55.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            ),
                            visualTransformation = PasswordVisualTransformation()
                        )
                        erro.value?.let {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = it, color = Color.Red)
                        }
                    }
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(140.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                if (email.value.isBlank() || senha.value.isBlank()) {
                                    erro.value = "Preencha todos os campos"
                                    return@Button
                                }

                                val usuarioService = RetrofitFactory().getUsuarioService()

                                usuarioService.listarUsuarios().enqueue(object : Callback<Result> {
                                    override fun onResponse(call: Call<Result>, response: Response<Result>) {
                                        if (response.isSuccessful) {
                                            val usuarios = response.body()?.usuario ?: emptyList()
                                            val usuarioEncontrado = usuarios.find {
                                                it.email == email.value && it.senha == senha.value
                                            }

                                            if (usuarioEncontrado != null) {
                                                erro.value = "Login realizado com sucesso"
                                                SharedPrefHelper.salvarEmail(context, usuarioEncontrado.email)
                                                navegacao?.navigate("home")
                                            } else {
                                                erro.value = "Email ou senha incorretos"
                                            }
                                        } else {
                                            erro.value = "Erro ao buscar usuários"
                                        }
                                    }

                                    override fun onFailure(call: Call<Result>, t: Throwable) {
                                        erro.value = "Erro de rede: ${t.message}"
                                    }
                                })


                            },
                            shape = RoundedCornerShape(48.dp),
                            modifier = Modifier
                                .width(200.dp)
                                .height(40.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF037EF7))
                        ) {
                            Text(text = "Logar", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Button(
                            onClick = {
                                navegacao?.navigate("recuperar_senha")
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            elevation = ButtonDefaults.buttonElevation(defaultElevation = 0.dp),
                            modifier = Modifier.height(35.dp),
                            shape = RoundedCornerShape(0.dp)
                        ) {
                            Text(
                                text = "Esqueceu a senha?",
                                fontSize = 15.sp,
                                color = Color(0xFF037EF7)
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "Não tem uma conta?", fontSize = 15.sp)
                            Button(
                                modifier = Modifier.height(35.dp),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                onClick = {
                                    navegacao?.navigate("cadastro")
                                }) {
                                Text(text = "Cadastre-se", fontSize = 15.sp, color = Color(0xFF037EF7))
                            }
                        }
                    }
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginPreview() {
    Login(navegacao = null)
}
