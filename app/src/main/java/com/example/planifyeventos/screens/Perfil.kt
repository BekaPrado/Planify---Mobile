package com.example.planifyeventos.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planifyeventos.model.Usuario
import com.example.planifyeventos.model.Result
import com.example.planifyeventos.screens.components.UsuarioCard
import com.example.planifyeventos.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.ui.Alignment
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import com.example.planifyeventos.R
import com.example.planifyeventos.utils.SharedPrefHelper

@Composable
fun Perfil(navegacao: NavHostController) {
    val usuarioLogado = remember { mutableStateOf<Usuario?>(null) }
    val loading = remember { mutableStateOf(true) }
    val errorMessage = remember { mutableStateOf<String?>(null) }
    val context = LocalContext.current

    val emailSalvo = SharedPrefHelper.recuperarEmail(context)

    LaunchedEffect(Unit) {
        val callUsuario = RetrofitFactory()
            .getUsuarioService()
            .listarUsuarios()

        callUsuario.enqueue(object : Callback<Result> {
            override fun onResponse(call: Call<Result>, response: Response<Result>) {
                loading.value = false
                if (response.isSuccessful) {
                    val usuarioEncontrado = response.body()?.usuario?.find {
                        it.email == emailSalvo
                    }
                    if (usuarioEncontrado != null) {
                        usuarioLogado.value = usuarioEncontrado
                        errorMessage.value = null
                    } else {
                        errorMessage.value = "Usuário não encontrado."
                    }
                } else {
                    errorMessage.value = "Erro ao carregar usuário: ${response.code()}"
                }
            }

            override fun onFailure(call: Call<Result>, t: Throwable) {
                loading.value = false
                errorMessage.value = "Falha na conexão: ${t.message}"
            }
        })
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp)
    ) {
        when {
            loading.value -> {
                CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            }

            errorMessage.value != null -> {
                Text(
                    text = errorMessage.value ?: "Erro desconhecido",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            usuarioLogado.value != null -> {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Image(
                            painter = painterResource(R.drawable.logo),
                            contentDescription = "",
                            modifier = Modifier
                                .width(100.dp)
                        )
                        Button(
                            onClick = {
                                SharedPrefHelper.deslogar(context)
                                navegacao.navigate("login") {
                                    popUpTo("perfil") { inclusive = true }
                                }
                            }
                        ) {
                            Text("Sair")
                        }
                    }
                    UsuarioCard(
                        nome = usuarioLogado.value!!.nome,
                        email = usuarioLogado.value!!.email,
                        data_nascimento = usuarioLogado.value!!.data_nascimento,
                        foto_perfil = usuarioLogado.value!!.foto_perfil,
                        senha = usuarioLogado.value!!.senha
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun PerfilPreview() {
    val fakeNavController = rememberNavController()
    Perfil(navegacao = fakeNavController)
}