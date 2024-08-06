package com.recursoconfiable.logisticabarrios

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://67b3-2806-2f0-9585-670e-39c4-2929-cc63-bdbf.ngrok-free.app"

    val instance: Retrofit
        get() {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

        }}