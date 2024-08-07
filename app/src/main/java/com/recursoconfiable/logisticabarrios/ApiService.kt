package com.recursoconfiable.logisticabarrios

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.POST
import retrofit2.http.Multipart
import retrofit2.http.Part

interface ApiService {
    @GET("/arribos/{usuarioId}")
    fun getArribos(@Path("usuarioId") usuarioId: Int): Call<List<Arribo>>

    @GET("/facturas_pendientes/{usuarioId}")
    fun getFacturasPendientes(@Path("usuarioId") usuarioId: Int): Call<List<Factura>>

    @GET("/arribos_pendientes/{usuarioId}")
    suspend fun getPendingArribosCount(@Path("usuarioId") usuarioId: Int): Response<PendingArribosResponse>

    @GET("/facturas_pendientes_count/{usuarioId}")
    suspend fun getPendingFacturasCount(@Path("usuarioId") usuarioId: Int): Response<PendingFacturasResponse>

    @GET("/arribo/{id}")
    fun getArriboById(@Path("id") id: Int): Call<Arribo>

    @Multipart
    @POST("uploadEvidencia")
    fun uploadEvidencia(
        @Part("idArribo") idArribo: RequestBody,
        @Part("nombreConductor") nombreConductor: RequestBody,
        @Part("telefonoConductor") telefonoConductor: RequestBody,
        @Part("placaTracto") placaTracto: RequestBody,
        @Part("valorEntero") valorEntero: RequestBody,
        @Part("marchamo") marchamo: RequestBody,
        @Part("marchamoDanado") marchamoDanado: RequestBody,
        @Part("fotoMarchamo") fotoMarchamo: MultipartBody.Part,
        @Part("fotoPuertas") fotoPuertas: MultipartBody.Part
    ): Call<Void>
}


data class PendingArribosResponse(val count: Int)
data class PendingFacturasResponse(val count: Int)

