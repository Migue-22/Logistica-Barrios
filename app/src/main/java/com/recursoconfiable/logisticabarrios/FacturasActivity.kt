package com.recursoconfiable.logisticabarrios

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class FacturasActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FacturaAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_facturas)

        recyclerView = findViewById(R.id.recyclerViewFacturas)
        recyclerView = findViewById(R.id.recyclerViewFacturas)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1)

        if (usuarioId != -1) {
            fetchFacturas(usuarioId)
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_LONG).show()
        }
    }
    // Actualiza la vista del RecyclerView con la lista de facturas.
    private fun updateFacturasView(facturas: List<Factura>) {
        adapter = FacturaAdapter(facturas) { factura ->
            // Crea un Intent para abrir la actividad DigitalizadorFacturas.
            val intent = Intent(this, DigitalizadorFacturas::class.java).apply {
                // Pasa el ID de la factura o cualquier otro dato necesario a la nueva actividad.
                putExtra("FACTURA_ID", factura.id)
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }


    private fun fetchFacturas(usuarioId: Int) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        apiService.getFacturasPendientes(usuarioId).enqueue(object : Callback<List<Factura>> {
            override fun onResponse(call: Call<List<Factura>>, response: Response<List<Factura>>) {
                if (response.isSuccessful) {
                    val facturas = response.body() ?: emptyList()
                    updateFacturasView(facturas)
                }
            }

            override fun onFailure(call: Call<List<Factura>>, t: Throwable) {
                Toast.makeText(this@FacturasActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
