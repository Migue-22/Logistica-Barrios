package com.recursoconfiable.logisticabarrios

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("arribos/{usuarioId}")
    fun getArribos(@Path("usuarioId") usuarioId: Int): Call<List<Arribo>>

    @GET("facturas_pendientes/{usuarioId}")
    fun getFacturasPendientes(@Path("usuarioId") usuarioId: Int): Call<List<Factura>>

    @GET("/arribos_pendientes/{usuarioId}")
    suspend fun getPendingArribosCount(@Path("usuarioId") usuarioId: Int): Response<PendingArribosResponse>

    @GET("/facturas_pendientes_count/{usuarioId}")
    suspend fun getPendingFacturasCount(@Path("usuarioId") usuarioId: Int): Response<PendingFacturasResponse>
}

data class PendingArribosResponse(val count: Int)
data class PendingFacturasResponse(val count: Int)

