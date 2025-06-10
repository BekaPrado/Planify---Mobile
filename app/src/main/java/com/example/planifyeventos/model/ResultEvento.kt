package com.example.planifyeventos.model

data class ResultEvento(
    val status: Boolean,
    val status_code: Int,
    val eventos: List<Evento>
)
