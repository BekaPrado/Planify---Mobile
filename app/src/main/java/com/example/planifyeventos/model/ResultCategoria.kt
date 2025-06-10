package com.example.planifyeventos.model

data class ResultCategoria(
    val status: Boolean,
    val status_code: Int,
    val itens: Int,
    val categoria: List<Categoria>
)
