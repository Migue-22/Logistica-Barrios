package com.recursoconfiable.logisticabarrios

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://bb66-2806-2f0-9585-670e-54a0-37a4-7c0a-fb98.ngrok-free.app"

    val instance: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    val apiService: ApiService
        get() = instance.create(ApiService::class.java)
    val apiServiceEvidencias: ApiServiceEvidencias
        get() = instance.create(ApiServiceEvidencias::class.java)
}
