package com.example.planifyeventos.service

import com.example.planifyeventos.model.EmailRequest
import com.example.planifyeventos.model.ValidacaoResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface EmailService {
    @POST("recuperar-senha")
    fun enviarEmail(@Body request: EmailRequest): Call<Void>

    @POST("validar-codigo")
    fun validarCodigo(@Body body: Map<String, String>): Call<ValidacaoResponse>
}
