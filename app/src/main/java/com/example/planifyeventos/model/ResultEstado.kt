package com.example.planifyeventos.model

data class ResultEstado(
    val status: Boolean,
    val status_code: Int,
    val itens: Int,
    val estado: List<Estado> // ⚠️ nome do campo é "categoria" no JSON mesmo sendo estado
)
