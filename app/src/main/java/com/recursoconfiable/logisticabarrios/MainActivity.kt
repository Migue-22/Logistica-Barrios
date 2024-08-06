package com.recursoconfiable.logisticabarrios

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

// La clase MainActivity hereda de AppCompatActivity, lo que significa que es una Activity que
// puede utilizar características compatibles con versiones anteriores de Android.
class MainActivity : AppCompatActivity() {

    // El método onCreate se llama cuando se crea la actividad.
    // Es el lugar donde debes inicializar tu actividad.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Establece el diseño de la actividad usando el archivo de diseño activity_main.xml.
        setContentView(R.layout.activity_main)

        // Encuentra el botón con el ID 'Inicio' en el archivo de diseño.
        val button = findViewById<Button>(R.id.Inicio)
        // Configura un listener para el botón que se activará cuando el usuario haga clic en él.
        button.setOnClickListener {
            // Crea un Intent para iniciar la actividad LogIn.
            val intent = Intent(this, LogInActivity::class.java)
            // Inicia la actividad LogIn.
            startActivity(intent)
        }
    }
}
