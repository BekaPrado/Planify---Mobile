package com.example.planifyeventos.model

data class Usuario(
    val id_usuario: Int = 0,
    val nome: String = "",
    val email: String = "",
    val senha: String = "",
    val data_nascimento: String = "",
    val palavra_chave: String = "",
    val foto_perfil: String = ""
)
data class SenhaRequest(
    val senha: String = ""
)