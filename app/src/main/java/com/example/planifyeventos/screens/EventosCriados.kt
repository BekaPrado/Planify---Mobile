package com.example.planifyeventos.screens
import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
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
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun EventosCriados(navegacao: NavHostController) {
    val context = LocalContext.current
    val eventos = remember { mutableStateListOf<Evento>() }
    val usuarioLogado = remember { mutableStateOf<Usuario?>(null) }
    val loading = remember { mutableStateOf(true) }

    fun carregarEventos() {
        RetrofitFactory().getEventoService().listarTodosEventos().enqueue(object : Callback<ResultEvento> {
            override fun onResponse(call: Call<ResultEvento>, response: Response<ResultEvento>) {
                eventos.clear()
                eventos.addAll(response.body()?.eventos ?: emptyList())
                loading.value = false
            }
            override fun onFailure(call: Call<ResultEvento>, t: Throwable) {
                Log.e("API", "Erro: ${t.message}")
                loading.value = false
            }
        })
    }

    fun excluirEvento(id: Int) {
        RetrofitFactory().getEventoService().excluirEvento(id).enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                carregarEventos()
            }
            override fun onFailure(call: Call<Void>, t: Throwable) {}
        })
    }

    LaunchedEffect(Unit) {
        val email = SharedPrefHelper.recuperarEmail(context)
        RetrofitFactory().getUsuarioService().listarUsuarios().enqueue(object : Callback<com.example.planifyeventos.model.Result> {
            override fun onResponse(call: Call<com.example.planifyeventos.model.Result>, response: Response<com.example.planifyeventos.model.Result>) {
                val usuario = response.body()?.usuario?.find { it.email == email }
                usuarioLogado.value = usuario
                carregarEventos()
            }
            override fun onFailure(call: Call<com.example.planifyeventos.model.Result>, t: Throwable) {}
        })
    }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Meus Eventos Criados", style = MaterialTheme.typography.titleLarge)
        NavegacaoBotoes(navegacao)
        Spacer(modifier = Modifier.height(16.dp))
        Button(onClick = { navegacao.navigate("perfil") }) { Text("Voltar para Perfil") }

        Text("Loading: ${loading.value}")
        Text("Usuário: ${usuarioLogado.value?.nome ?: "null"}")
        Text("Eventos carregados: ${eventos.size}")

        if (loading.value) {
            CircularProgressIndicator()
        } else if (usuarioLogado.value == null) {
            Text("Usuário não encontrado ou não logado.", color = Color.Red)
        } else {
            val meusEventos = eventos.filter {
                it.id_usuario == usuarioLogado.value!!.id_usuario
            }

            if (meusEventos.isEmpty()) {
                Text("Você ainda não criou nenhum evento.", color = Color.Gray)
            } else {
                meusEventos.forEach { evento ->
                    EventoCard(evento = evento, exibirExcluir = true) {
                        excluirEvento(evento.id_evento)
                    }
                }
            }
        }
    }
}
