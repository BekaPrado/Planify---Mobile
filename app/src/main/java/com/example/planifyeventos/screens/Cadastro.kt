package com.example.planifyeventos.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planifyeventos.R
import com.example.planifyeventos.model.Usuario
import com.example.planifyeventos.service.RetrofitFactory
import com.example.planifyeventos.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Toast

@Composable
fun Cadastro(navegacao: NavHostController) {
    val nome = remember { mutableStateOf("") }
    val dataNascimento = remember { mutableStateOf("") }
    val email = remember { mutableStateOf("") }
    val senha = remember { mutableStateOf("") }
    val confirmarSenha = remember { mutableStateOf("") }
    val fotoPerfil = remember { mutableStateOf("") }
    val context = LocalContext.current
    fun formatarDataParaIso(data: String): String {
        return try {
            when {
                data.contains("/") -> {
                    // Entrada: "19/04/2008"
                    val partes = data.split("/")
                    val dia = partes[0]
                    val mes = partes[1]
                    val ano = partes[2]
                    "$ano-$mes-$dia"
                }

                data.length == 8 -> {
                    // Entrada: "19042008"
                    val dia = data.substring(0, 2)
                    val mes = data.substring(2, 4)
                    val ano = data.substring(4, 8)
                    "$ano-$mes-$dia"
                }

                else -> data // Retorna como está se o formato for desconhecido
            }
        } catch (e: Exception) {
            data // Retorna original em caso de erro
        }
    }

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
                border = BorderStroke(14.dp, Color(0xFF008EFF))
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
                        Text("Nome:", fontSize = 15.sp)
                        OutlinedTextField(
                            value = nome.value,
                            onValueChange = { nome.value = it },
                            shape = RoundedCornerShape(33.dp),
                            singleLine = true,
                            modifier = Modifier.height(50.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Data de nascimento:", fontSize = 15.sp)
                        OutlinedTextField(
                            value = dataNascimento.value,
                            onValueChange = { dataNascimento.value = it },
                            shape = RoundedCornerShape(33.dp),
                            singleLine = true,
                            modifier = Modifier
                                .height(50.dp)
                                .width(150.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Number,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Email:", fontSize = 15.sp)
                        OutlinedTextField(
                            value = email.value,
                            onValueChange = { email.value = it },
                            shape = RoundedCornerShape(33.dp),
                            singleLine = true,
                            modifier = Modifier.height(50.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Senha:", fontSize = 15.sp)
                        OutlinedTextField(
                            value = senha.value,
                            onValueChange = { senha.value = it },
                            shape = RoundedCornerShape(33.dp),
                            singleLine = true,
                            modifier = Modifier.height(50.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Next
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Confirmar Senha:", fontSize = 15.sp)
                        OutlinedTextField(
                            value = confirmarSenha.value,
                            onValueChange = { confirmarSenha.value = it },
                            shape = RoundedCornerShape(33.dp),
                            singleLine = true,
                            modifier = Modifier.height(50.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Password,
                                imeAction = ImeAction.Done
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))
                        Text("URL da foto de perfil:", fontSize = 15.sp)
                        OutlinedTextField(
                            value = fotoPerfil.value,
                            onValueChange = { fotoPerfil.value = it },
                            shape = RoundedCornerShape(33.dp),
                            singleLine = true,
                            modifier = Modifier.height(50.dp),
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text,
                                imeAction = ImeAction.Done
                            )
                        )
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(100.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Button(
                            onClick = {
                                // Validação mínima
                                if (nome.value.isBlank() || email.value.isBlank() || senha.value.isBlank()) {
                                    Toast.makeText(context, "Preencha todos os campos obrigatórios", Toast.LENGTH_SHORT).show()
                                    return@Button
                                }

                                val usuario = Usuario(
                                    nome = nome.value,
                                    email = email.value,
                                    senha = senha.value,
                                    data_nascimento = formatarDataParaIso(dataNascimento.value),
                                    foto_perfil = fotoPerfil.value
                                )


                                val call = RetrofitFactory().getUsuarioService().inserirUsuario(usuario)

                                call.enqueue(object : Callback<Usuario> {
                                    override fun onResponse(call: Call<Usuario>, response: Response<Usuario>) {
                                        if (response.isSuccessful) {
                                            Toast.makeText(context, "Cadastro realizado com sucesso!", Toast.LENGTH_SHORT).show()
                                            navegacao.navigate(route = "login")
                                        } else {
                                            val erroMsg = response.errorBody()?.string()
                                            val mensagem = if (erroMsg?.contains("email", ignoreCase = true) == true) {
                                                "Este e-mail já está cadastrado."
                                            } else {
                                                "Erro ao cadastrar. Tente novamente."
                                            }
                                            Log.e("API", "Erro ao cadastrar: código ${response.code()}, corpo: $erroMsg")
                                            Toast.makeText(context, mensagem, Toast.LENGTH_LONG).show()
                                        }
                                    }

                                    override fun onFailure(call: Call<Usuario>, t: Throwable) {
                                        Log.e("API", "Falha na requisição: ${t.message}", t)
                                        Toast.makeText(context, "Erro na conexão com o servidor.", Toast.LENGTH_LONG).show()
                                    }
                                })
                            },
                            modifier = Modifier
                                .width(200.dp)
                                .align(Alignment.CenterHorizontally),
                            shape = RoundedCornerShape(48.dp),
                            colors = ButtonDefaults.buttonColors(Color(0xFF008EFF))
                        ) {
                            Text("Cadastrar", fontSize = 15.sp, fontWeight = FontWeight.ExtraBold)
                        }


                        Spacer(modifier = Modifier.height(10.dp))
                        Row(
                            modifier = Modifier
                                .height(60.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text("Já tem uma conta?", fontSize = 15.sp)
                            Button(
                                modifier = Modifier.height(45.dp),
                                colors = ButtonDefaults.buttonColors(Color.Transparent),
                                onClick = { navegacao.navigate(route = "login") }
                            ) {
                                Text("Entrar", fontSize = 15.sp, color = Color(0xFF0C75FF))
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
private fun CadastroPreview() {
    val navController = rememberNavController()
    Cadastro(navegacao = navController)
}
