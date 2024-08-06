package com.recursoconfiable.logisticabarrios

// Importa las clases necesarias de Retrofit
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST


// Define una interfaz para el servicio API
interface Api {
    // Define un método para realizar una solicitud POST al endpoint /login
    @POST("/login")
    fun login(@Body request: LoginRequest): Call<LoginResponse>

}

// Definición de una clase de datos para la solicitud de login
data class LoginRequest(
    val username: String, // Nombre de usuario para el login
    val password: String  // Contraseña para el login
)