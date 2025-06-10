
package com.example.planifyeventos.service

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object  RetrofitEmailFactory {
    fun getEmailService(): EmailService {
        return Retrofit.Builder()
            .baseUrl("http://192.168.15.27:8080/v1/planify/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EmailService::class.java)
    }
}
