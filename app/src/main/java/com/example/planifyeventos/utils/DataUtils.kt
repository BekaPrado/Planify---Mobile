package com.example.planifyeventos.utils


import java.text.SimpleDateFormat
import java.util.*

fun String.formatarData(): String {
    return try {
        val formatoOriginal = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())
        val formatoDestino = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val data = formatoOriginal.parse(this)
        data?.let { formatoDestino.format(it) } ?: this
    } catch (e: Exception) {
        this // Em caso de erro, retorna a string original
    }
}