package com.example.planifyeventos.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PageSize
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.planifyeventos.R
import com.example.planifyeventos.model.*
import com.example.planifyeventos.screens.components.EventoCard
import com.example.planifyeventos.screens.components.MultiSelectDialog
import com.example.planifyeventos.service.RetrofitFactory
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

@Composable
fun Home(navegacao: NavHostController?) {
    val pagerState = rememberPagerState { 5 }
    val pagerItems = listOf(
        R.drawable.carrossel1, R.drawable.carrossel2, R.drawable.carrossel3,
        R.drawable.carrossel4, R.drawable.carrossel5
    )

    val context = LocalContext.current
    val eventosState = remember { mutableStateOf<List<Evento>>(emptyList()) }
    val eventosFiltrados = remember { mutableStateOf<List<Evento>>(emptyList()) }

    var showCategoriaDialog by remember { mutableStateOf(false) }
    var showEstadoDialog by remember { mutableStateOf(false) }

    var categorias by remember { mutableStateOf<List<Categoria>>(emptyList()) }
    var estados by remember { mutableStateOf<List<Estado>>(emptyList()) }

    var categoriasSelecionadas by remember { mutableStateOf<List<String>>(emptyList()) }
    var estadosSelecionados by remember { mutableStateOf<List<String>>(emptyList()) }

    fun aplicarFiltro() {
        eventosFiltrados.value = eventosState.value.filter { evento ->
            val categoriaOk = categoriasSelecionadas.isEmpty() ||
                    evento.categoria.any { cat ->
                        categoriasSelecionadas.any { filtro -> filtro.equals(cat.categoria, ignoreCase = true) }
                    }

            val estadoOk = estadosSelecionados.isEmpty() ||
                    estadosSelecionados.any { it.equals(evento.estado.estado, ignoreCase = true) }

            categoriaOk && estadoOk
        }
    }


    fun carregarCategorias() {
        RetrofitFactory().getEventoService().listarCategorias()
            .enqueue(object : Callback<ResultCategoria> {
                override fun onResponse(call: Call<ResultCategoria>, response: Response<ResultCategoria>) {
                    categorias = response.body()?.categoria ?: emptyList()
                    showCategoriaDialog = true
                }
                override fun onFailure(call: Call<ResultCategoria>, t: Throwable) {
                    Log.e("Home", "Erro ao carregar categorias: ${t.message}")
                }
            })
    }

    fun carregarEstados() {
        RetrofitFactory().getEventoService().listarEstados()
            .enqueue(object : Callback<ResultEstado> {
                override fun onResponse(call: Call<ResultEstado>, response: Response<ResultEstado>) {
                    estados = response.body()?.estado ?: emptyList() // campo mal nomeado no JSON
                    showEstadoDialog = true
                }
                override fun onFailure(call: Call<ResultEstado>, t: Throwable) {
                    Log.e("Home", "Erro ao carregar estados: ${t.message}")
                }
            })
    }

    LaunchedEffect(Unit) {
        RetrofitFactory().getEventoService().listarEventos()
            .enqueue(object : Callback<ResultEvento> {
                override fun onResponse(call: Call<ResultEvento>, response: Response<ResultEvento>) {
                    if (response.isSuccessful) {
                        eventosState.value = response.body()?.eventos ?: emptyList()
                        eventosFiltrados.value = eventosState.value
                    } else {
                        Log.e("Home", "Erro no código de resposta: ${response.code()}")
                    }
                }
                override fun onFailure(call: Call<ResultEvento>, t: Throwable) {
                    Log.e("Home", "Falha na requisição: ${t.message}")
                }
            })
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .padding(horizontal = 16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = "Logo",
                    modifier = Modifier.size(90.dp)
                )

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                        .background(Color(0xFFF0F0F0), RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                ) {
                    Text(
                        text = "Pesquisar shows, eventos, teatros",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Button(
                    onClick = { navegacao?.navigate("perfil") },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    contentPadding = PaddingValues(0.dp),
                    modifier = Modifier.size(64.dp)
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.pe),
                        contentDescription = "Botão com imagem"
                    )
                }
            }
        }

        item {
            HorizontalPager(
                state = pagerState,
                pageSize = PageSize.Fill,
                contentPadding = PaddingValues(horizontal = 32.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(250.dp)
            ) { page ->
                Image(
                    painter = painterResource(pagerItems[page]),
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(30.dp))
                        .clickable {
                            Toast.makeText(context, "Imagem: $page", Toast.LENGTH_SHORT).show()
                        },
                    contentScale = ContentScale.Crop
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                repeat(pagerState.pageCount) { index ->
                    val color = if (pagerState.currentPage == index) Color.DarkGray else Color.LightGray
                    Box(
                        modifier = Modifier
                            .padding(3.dp)
                            .clip(CircleShape)
                            .background(color)
                            .size(10.dp)
                    )
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Button(onClick = { carregarCategorias() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0025DA), contentColor = Color.White)) {
                    Text("Categoria")
                }
                Button(onClick = { carregarEstados() }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0025DA), contentColor = Color.White)) {
                    Text("Estado")
                }
                Button(onClick = { navegacao?.navigate("criar_evento") }, colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF0025DA), contentColor = Color.White)) {
                    Text("Criar Evento")
                }
            }

            Text(
                text = "Conheça os Eventos:",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp, bottom = 8.dp)
            )
        }

        items(eventosFiltrados.value) { evento ->
            EventoCard(evento)
        }
    }

    if (showCategoriaDialog) {
        MultiSelectDialog(
            titulo = "Categorias",
            opcoes = categorias.map { it.categoria },
            selecionados = categoriasSelecionadas,
            onConfirmar = {
                categoriasSelecionadas = it
                showCategoriaDialog = false
                aplicarFiltro()
            },
            onDismiss = { showCategoriaDialog = false }
        )
    }

    if (showEstadoDialog) {
        Log.d("DEBUG", "Estados disponíveis: ${estados.map { it.estado }}")

        MultiSelectDialog(
            titulo = "Estados",
            opcoes = estados.map { it.estado },
            selecionados = estadosSelecionados,
            onConfirmar = {
                estadosSelecionados = it
                showEstadoDialog = false
                aplicarFiltro()
            },
            onDismiss = { showEstadoDialog = false }
        )
    }
}

@Preview(showSystemUi = true)
@Composable
fun HomePreview() {
    Home(null)
}
