package com.example.planifyeventos.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitFactory {

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://192.168.15.27:8080/v1/planify/")  // ou IP real da sua máquina
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    fun getUsuarioService(): UsuarioService {
        return retrofit.create(UsuarioService::class.java)
    }

    // 👇 Recrie este método:
    fun getEventoService(): EventoService {
        return retrofit.create(EventoService::class.java)
    }

}
