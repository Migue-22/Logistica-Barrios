package com.recursoconfiable.logisticabarrios

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.animation.LinearInterpolator
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class MenuPrincipalActivity : AppCompatActivity() {

    private lateinit var buttonArribo: Button
    private lateinit var buttonFacturas: Button
    private lateinit var apiService: ApiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_menu_principal)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        buttonArribo = findViewById(R.id.ButtonArribo)
        buttonFacturas = findViewById(R.id.ButtonFacturas)

        buttonFacturas.setOnClickListener {
            val intent = Intent(this, FacturasActivity::class.java)
            startActivity(intent)
        }

        // Configura Retrofit
        val retrofit = Retrofit.Builder()
            .baseUrl("https://bb66-2806-2f0-9585-670e-54a0-37a4-7c0a-fb98.ngrok-free.app")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        apiService = retrofit.create(ApiService::class.java)

        // Obtiene el usuarioId desde SharedPreferences y verifica arribos pendientes
        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1)

        if (usuarioId != -1) {
            checkPendingArribos(usuarioId)
            checkPendingFacturas(usuarioId)
        } else {
            Toast.makeText(this, "ID de usuario no encontrado", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkPendingArribos(usuarioId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getPendingArribosCount(usuarioId)
                if (response.isSuccessful) {
                    val count = response.body()?.count ?: 0
                    withContext(Dispatchers.Main) {
                        updateButtonArribo(count)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MenuPrincipalActivity, "Error al obtener arribos pendientes", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MenuPrincipalActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateButtonArribo(count: Int) {
        if (count > 0) {
            buttonArribo.text = "Arribos ($count)"
            startBlinkingAnimation(buttonArribo)
        } else {
            buttonArribo.text = "Arribo"
            buttonArribo.clearAnimation()
        }

        buttonArribo.setOnClickListener {
            val intent = Intent(this, ArriboActivity::class.java)
            startActivity(intent)
        }
    }

    private fun startBlinkingAnimation(view: Button) {
        val greenColor = ContextCompat.getColor(this, R.color.greenButton)
        val redColor = ContextCompat.getColor(this, R.color.red)

        val colorAnimation = ValueAnimator.ofArgb(greenColor, redColor)
        colorAnimation.duration = 500 // Duración de un ciclo de parpadeo en milisegundos
        colorAnimation.repeatCount = ValueAnimator.INFINITE
        colorAnimation.repeatMode = ValueAnimator.REVERSE
        colorAnimation.interpolator = LinearInterpolator()

        colorAnimation.addUpdateListener { animator ->
            view.setBackgroundColor(animator.animatedValue as Int)
        }

        colorAnimation.start()
    }

    private fun checkPendingFacturas(usuarioId: Int) {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val response = apiService.getPendingFacturasCount(usuarioId)
                if (response.isSuccessful) {
                    val count = response.body()?.count ?: 0
                    withContext(Dispatchers.Main) {
                        updateButtonFacturas(count)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(this@MenuPrincipalActivity, "Error al obtener facturas pendientes", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(this@MenuPrincipalActivity, "Error de conexión", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun updateButtonFacturas(count: Int) {
        if (count > 0) {
            buttonFacturas.text = "Digitalización de Facturas ($count)"
            startBlinkingAnimation(buttonFacturas)
        } else {
            buttonFacturas.text = "Digitalización de Facturas"
            buttonFacturas.clearAnimation()
        }

        buttonFacturas.setOnClickListener {
            val intent = Intent(this, FacturasActivity::class.java)
            startActivity(intent)
        }
    }
}
