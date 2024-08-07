package com.recursoconfiable.logisticabarrios

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServiceEvidencias {
    @Multipart
    @POST("/evidencias")
    fun uploadEvidencia(
        @Part("idArribo") idArribo: RequestBody,
        @Part("nombre") nombre: RequestBody,
        @Part("telefono") telefono: RequestBody,
        @Part("placa") placa: RequestBody,
        @Part("cero") cero: RequestBody,
        @Part("marchamo") marchamo: RequestBody,
        @Part("marchamoDanado") marchamoDañado: RequestBody,
        @Part photoMarchamo: MultipartBody.Part?,
        @Part photoPuertas: MultipartBody.Part?
    ): Call<Void>
}



