package com.example.planifyeventos.service

import com.example.planifyeventos.model.Result
import com.example.planifyeventos.model.Usuario
import retrofit2.Call
import retrofit2.http.*

interface UsuarioService {

    @Headers("Content-Type: application/json")
    @POST("usuario")
    fun inserirUsuario(@Body usuario: Usuario): Call<Usuario>

    @GET("usuario")
    fun listarUsuarios(): Call<Result>

    @GET("usuario/{id}")
    fun listarUsuarioPorId(@Path("id") id: Int): Call<Usuario>

    @PUT("usuario/{id}")
    fun atualizarUsuarioPorId(@Path("id") id: Int): Call<Usuario>

    @PUT("usuario/senha/{id}")
    fun redefinirSenhaRaw(
        @Path("id") id: Int,
        @Body body: okhttp3.RequestBody
    ): Call<Void>



}
