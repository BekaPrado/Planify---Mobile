package com.example.planifyeventos.screens

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.planifyeventos.model.Evento
import com.example.planifyeventos.model.ResultEvento
import com.example.planifyeventos.model.Usuario
import com.example.planifyeventos.screens.components.NavegacaoBotoes
import com.example.planifyeventos.screens.components.EventoCard
import com.example.planifyeventos.service.RetrofitFactory
import com.example.planifyeventos.utils.SharedPrefHelper
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Ingressos(navegacao: NavHostController) {
    val context = LocalContext.current
    val eventos = remember { mutableStateListOf<Evento>() }
    val usuarioLogado = remember { mutableStateOf<Usuario?>(null) }
    val loading = remember { mutableStateOf(true) }

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    fun carregarEventos() {
        RetrofitFactory().getEventoService().listarTodosEventos().enqueue(object : Callback<ResultEvento> {
            override fun onResponse(call: Call<ResultEvento>, response: Response<ResultEvento>) {
                eventos.clear()
                eventos.addAll(response.body()?.eventos ?: emptyList())
                loading.value = false
            }

            override fun onFailure(call: Call<ResultEvento>, t: Throwable) {
                Log.e("API", "Erro ao carregar eventos: ${t.message}")
                scope.launch {
                    snackbarHostState.showSnackbar("Erro ao carregar eventos: ${t.message}")
                }
                loading.value = false
            }
        })
    }

    LaunchedEffect(Unit) {
        val email = SharedPrefHelper.recuperarEmail(context)
        RetrofitFactory().getUsuarioService().listarUsuarios()
            .enqueue(object : Callback<com.example.planifyeventos.model.Result> {
                override fun onResponse(
                    call: Call<com.example.planifyeventos.model.Result>,
                    response: Response<com.example.planifyeventos.model.Result>
                ) {
                    val usuario = response.body()?.usuario?.find { it.email == email }
                    usuarioLogado.value = usuario
                    if (usuario == null) {
                        scope.launch {
                            snackbarHostState.showSnackbar("Usuário não encontrado.")
                        }
                    } else {
                        carregarEventos()
                    }
                }

                override fun onFailure(call: Call<com.example.planifyeventos.model.Result>, t: Throwable) {
                    scope.launch {
                        snackbarHostState.showSnackbar("Erro ao carregar usuário: ${t.message}")
                    }
                    loading.value = false
                }
            })
    }

    Scaffold(
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { paddingValues ->

        Column(modifier = Modifier.padding(paddingValues).padding(16.dp)) {
            Text("Meus Ingressos", style = MaterialTheme.typography.titleLarge)
            NavegacaoBotoes(navegacao)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { navegacao.navigate("perfil") }) {
                Text("Voltar para Perfil")
            }

            if (!loading.value) {
                val usuario = usuarioLogado.value

                if (usuario?.id_usuario != null) {
                    val idUsuario = usuario.id_usuario

                    val eventosFiltrados = eventos.filter { evento ->
                        evento.participante?.any { p -> p.id_usuario == idUsuario } == true
                    }

                    if (eventosFiltrados.isEmpty()) {
                        Text("Você ainda não possui ingressos.")
                    } else {
                        eventosFiltrados.forEach { evento ->
                            EventoCard(evento = evento, exibirExcluir = true) {
                                val dados = mapOf(
                                    "id_usuario" to idUsuario,
                                    "id_evento" to evento.id_evento
                                )

                                RetrofitFactory().getEventoService().sairDoEvento(dados)
                                    .enqueue(object : Callback<Void> {
                                        override fun onResponse(call: Call<Void>, response: Response<Void>) {
                                            if (response.isSuccessful) {
                                                eventos.remove(evento)
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Você saiu do evento com sucesso.")
                                                }
                                            } else {
                                                scope.launch {
                                                    snackbarHostState.showSnackbar("Erro ao sair do evento: ${response.code()}")
                                                }
                                            }
                                        }

                                        override fun onFailure(call: Call<Void>, t: Throwable) {
                                            scope.launch {
                                                snackbarHostState.showSnackbar("Erro de conexão: ${t.message}")
                                            }
                                        }
                                    })
                            }
                        }
                    }
                } else {
                    scope.launch {
                        snackbarHostState.showSnackbar("Erro: Usuário não carregado corretamente.")
                    }
                }
            }
        }
    }
}
