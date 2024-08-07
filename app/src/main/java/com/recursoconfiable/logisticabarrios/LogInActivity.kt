package com.recursoconfiable.logisticabarrios

import android.content.Intent
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Actividad que maneja el login de los usuarios.
class LogInActivity : AppCompatActivity() {

    // Declaración de la instancia de Api.
    private lateinit var api: Api

    // Método llamado cuando se crea la actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el layout de la actividad.
        setContentView(R.layout.activity_log_in)

        // Configura Retrofit para manejar solicitudes HTTP.
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bb66-2806-2f0-9585-670e-54a0-37a4-7c0a-fb98.ngrok-free.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        // Crea una instancia de la interfaz Api.
        api = retrofit.create(Api::class.java)

        // Configura el botón de login.
        val loginButton: Button = findViewById(R.id.loginButton)
        loginButton.setOnClickListener {
            performLogin()
        }

        // Configura el campo de contraseña y el botón para alternar la visibilidad.
        val passwordEditText: EditText = findViewById(R.id.password)
        val togglePasswordVisibility: ImageButton = findViewById(R.id.togglePasswordVisibility)

        // Configura el clic del botón para alternar la visibilidad de la contraseña.
        togglePasswordVisibility.setOnClickListener {
            // Verifica si la contraseña está visible.
            val isPasswordVisible = passwordEditText.transformationMethod == null

            Log.d("PasswordVisibility", "Is password visible: $isPasswordVisible")

            // Alterna la visibilidad de la contraseña.
            if (isPasswordVisible) {
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility_off)
            } else {
                passwordEditText.transformationMethod = null
                togglePasswordVisibility.setImageResource(R.drawable.ic_visibility)
            }

            // Mueve el cursor al final del texto en el campo de contraseña.
            passwordEditText.post {
                passwordEditText.setSelection(passwordEditText.text.length)
                passwordEditText.postInvalidate()
            }
        }

        // Configura la visibilidad de los insets de la ventana para ajustar el padding.
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
    }

    // Método para realizar la solicitud de login.
    private fun performLogin() {
        // Obtiene el nombre de usuario y la contraseña de los campos de texto.
        val username = findViewById<EditText>(R.id.username).text.toString()
        val password = findViewById<EditText>(R.id.password).text.toString()

        // Crea un objeto LoginRequest con las credenciales del usuario.
        val loginRequest = LoginRequest(username, password)
        // Realiza la solicitud de login a la API.
        api.login(loginRequest).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                // Maneja la respuesta de la solicitud.
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    // Verifica si el login fue exitoso.
                    if (loginResponse?.success == true) {
                        val usuarioId = loginResponse.user?.id ?: -1

                        // Guarda el ID del usuario en SharedPreferences.
                        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
                        val editor = sharedPreferences.edit()
                        editor.putInt("USER_ID", usuarioId)
                        editor.apply()

                        // Navega a la actividad principal.
                        val intent = Intent(this@LogInActivity, MenuPrincipalActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        // Muestra un mensaje de error si el login no fue exitoso.
                        Toast.makeText(this@LogInActivity, loginResponse?.message ?: "Error desconocido", Toast.LENGTH_LONG).show()
                    }
                } else {
                    // Muestra un mensaje de error si la respuesta no es exitosa.
                    Toast.makeText(this@LogInActivity, "Error: ${response.code()}", Toast.LENGTH_LONG).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                // Muestra un mensaje de error si la solicitud falla.
                Toast.makeText(this@LogInActivity, "Request failed: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
