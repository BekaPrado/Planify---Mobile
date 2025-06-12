package com.example.planifyeventos.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.rememberNavController
import com.example.planifyeventos.R
import com.example.planifyeventos.model.*
import com.example.planifyeventos.service.RetrofitFactory
import com.example.planifyeventos.utils.SharedPrefHelper
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CriarEvento(navegacao: NavHostController) {
    val context = LocalContext.current
    val idUsuario = SharedPrefHelper.obterIdUsuario(context)

    data class EventoFormState(
        var urlImagem: String = "",
        var titulo: String = "",
        var dataEvento: String = "",
        var horarioEvento: String = "",
        var localEvento: String = "",
        var limite: String = "",
        var categoria: String = "",
        var estado: String = "",
        var descricao: String = "",
        var valor: String = ""
    )

    val formState = remember { mutableStateOf(EventoFormState()) }
    val listaEstados = remember { mutableStateListOf<Estado>() }
    val estadoExpandido = remember { mutableStateOf(false) }
    val estadoSelecionado = remember { mutableStateOf("") }

    val listaCategorias = remember { mutableStateListOf<Categoria>() }
    val categoriaExpandido = remember { mutableStateOf(false) }
    val categoriaSelecionada = remember { mutableStateOf("") }

    LaunchedEffect(Unit) {
        RetrofitFactory().getEventoService().listarEstados().enqueue(object : Callback<ResultEstado> {
            override fun onResponse(call: Call<ResultEstado>, response: Response<ResultEstado>) {
                if (response.isSuccessful) {
                    listaEstados.clear()
                    listaEstados.addAll(response.body()?.estado ?: emptyList())
                }
            }
            override fun onFailure(call: Call<ResultEstado>, t: Throwable) {
                Log.e("API", "Erro ao carregar estados: ${t.message}")
            }
        })

        RetrofitFactory().getEventoService().listarCategorias().enqueue(object : Callback<ResultCategoria> {
            override fun onResponse(call: Call<ResultCategoria>, response: Response<ResultCategoria>) {
                if (response.isSuccessful) {
                    listaCategorias.clear()
                    listaCategorias.addAll(response.body()?.categoria ?: emptyList())
                }
            }
            override fun onFailure(call: Call<ResultCategoria>, t: Throwable) {
                Log.e("API", "Erro ao carregar categorias: ${t.message}")
            }
        })
    }

    fun validarCampos(): Boolean {
        return when {
            formState.value.titulo.isBlank() -> {
                Toast.makeText(context, "Título é obrigatório", Toast.LENGTH_SHORT).show()
                false
            }
            formState.value.dataEvento.isBlank() -> {
                Toast.makeText(context, "Data é obrigatória", Toast.LENGTH_SHORT).show()
                false
            }
            formState.value.horarioEvento.isBlank() -> {
                Toast.makeText(context, "Horário é obrigatório", Toast.LENGTH_SHORT).show()
                false
            }
            formState.value.localEvento.isBlank() -> {
                Toast.makeText(context, "Local é obrigatório", Toast.LENGTH_SHORT).show()
                false
            }
            formState.value.categoria.isBlank() -> {
                Toast.makeText(context, "Categoria é obrigatória", Toast.LENGTH_SHORT).show()
                false
            }
            formState.value.estado.isBlank() -> {
                Toast.makeText(context, "Estado é obrigatório", Toast.LENGTH_SHORT).show()
                false
            }
            else -> true
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(onClick = { navegacao.navigate("home") }, colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)) {
            Image(painter = painterResource(R.drawable.logo), contentDescription = "logo", modifier = Modifier.width(100.dp))
        }

        OutlinedTextField(
            value = formState.value.urlImagem,
            onValueChange = { formState.value = formState.value.copy(urlImagem = it) },
            label = { Text("URL da Imagem do Evento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.value.titulo,
            onValueChange = { formState.value = formState.value.copy(titulo = it) },
            label = { Text("Nome do Evento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(expanded = categoriaExpandido.value, onExpandedChange = { categoriaExpandido.value = !categoriaExpandido.value }) {
            OutlinedTextField(
                value = categoriaSelecionada.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Categoria") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoriaExpandido.value) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(expanded = categoriaExpandido.value, onDismissRequest = { categoriaExpandido.value = false }) {
                listaCategorias.forEach { categoria ->
                    DropdownMenuItem(
                        text = { Text(categoria.categoria) },
                        onClick = {
                            categoriaSelecionada.value = categoria.categoria
                            formState.value = formState.value.copy(categoria = categoria.id_categoria.toString())
                            categoriaExpandido.value = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        ExposedDropdownMenuBox(expanded = estadoExpandido.value, onExpandedChange = { estadoExpandido.value = !estadoExpandido.value }) {
            OutlinedTextField(
                value = estadoSelecionado.value,
                onValueChange = {},
                readOnly = true,
                label = { Text("Estado") },
                trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = estadoExpandido.value) },
                modifier = Modifier.menuAnchor().fillMaxWidth(),
                shape = RoundedCornerShape(12.dp)
            )
            ExposedDropdownMenu(expanded = estadoExpandido.value, onDismissRequest = { estadoExpandido.value = false }) {
                listaEstados.forEach { estado ->
                    DropdownMenuItem(
                        text = { Text(estado.estado) },
                        onClick = {
                            estadoSelecionado.value = estado.estado
                            formState.value = formState.value.copy(estado = estado.id_estado.toString())
                            estadoExpandido.value = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
            value = formState.value.dataEvento,
            onValueChange = { formState.value = formState.value.copy(dataEvento = it) },
            label = { Text("Data do Evento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = formState.value.horarioEvento,
            onValueChange = { formState.value = formState.value.copy(horarioEvento = it) },
            label = { Text("Horário") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = formState.value.localEvento,
            onValueChange = { formState.value = formState.value.copy(localEvento = it) },
            label = { Text("Local") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = formState.value.limite,
            onValueChange = { formState.value = formState.value.copy(limite = it) },
            label = { Text("Limite") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = formState.value.descricao,
            onValueChange = { formState.value = formState.value.copy(descricao = it) },
            label = { Text("Descrição do evento") },
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        OutlinedTextField(
            value = formState.value.valor,
            onValueChange = { formState.value = formState.value.copy(valor = it) },
            label = { Text("Valor") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            if (!validarCampos()) return@Button

            val evento = Evento(
                titulo = formState.value.titulo,
                descricao = formState.value.descricao,
                data_evento = formState.value.dataEvento,
                horario = formState.value.horarioEvento,
                local = formState.value.localEvento,
                imagem = if (formState.value.urlImagem.isBlank()) null else formState.value.urlImagem,
                limite_participante = formState.value.limite.toIntOrNull() ?: 0,
                valor_ingresso = formState.value.valor.toDoubleOrNull() ?: 0.0,
                id_estado = formState.value.estado,
                id_categoria = formState.value.categoria,
                id_usuario = idUsuario,
                participante = emptyList()
            )

            RetrofitFactory().getEventoService().inserirEvento(evento).enqueue(object : Callback<Evento> {
                override fun onResponse(call: Call<Evento>, response: Response<Evento>) {
                    if (response.isSuccessful) {
                        Toast.makeText(context, "Evento criado com sucesso!", Toast.LENGTH_LONG).show()
                        navegacao.popBackStack()
                    } else {
                        Toast.makeText(context, "Erro ao cadastrar evento", Toast.LENGTH_LONG).show()
                    }
                }

                override fun onFailure(call: Call<Evento>, t: Throwable) {
                    Toast.makeText(context, "Falha de conexão: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }, modifier = Modifier.fillMaxWidth()) {
            Text("Cadastrar Evento")
        }
    }
}


@Composable
private fun CriarEventoPreview() {
    val navController = rememberNavController()
    CriarEvento(navegacao = navController)
}
