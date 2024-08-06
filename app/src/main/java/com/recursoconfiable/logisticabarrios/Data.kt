package com.recursoconfiable.logisticabarrios

// Definición de la clase de datos LoginResponse que representa la respuesta de una solicitud de login
data class LoginResponse(
    val success: Boolean, // Indica si la solicitud de login fue exitosa
    val message: String,  // Mensaje de la respuesta, puede ser un mensaje de error o éxito
    val user: User?       // Datos del usuario si el login fue exitoso, puede ser nulo si el login falla
)

// Definición de la clase de datos User que representa la información de un usuario
data class User(
    val id: Int,        // Identificador único del usuario
    val nombre: String, // Nombre del usuario
    val usuario: String // Nombre de usuario para el login
)