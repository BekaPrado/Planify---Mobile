package com.example.planifyeventos.model

data class Evento(
    val id_evento: Int,
    val titulo: String,
    val descricao: String,
    val data_evento: String,
    val horario: String,
    val local: String,
    val imagem: String,
    val limite_participante: String,
    val valor_ingresso: String,
    val estado: Estado,
    val categoria: List<Categoria>, // 👈 LISTA!
    val usuario: List<Usuario>?,     // se aplicável
    val participante: List<Usuario>? // se aplicável
)
