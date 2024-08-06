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

// La actividad que maneja la vista de arribos.
class ArriboActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ArriboAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arribo)

        recyclerView = findViewById(R.id.recyclerViewArribos)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val sharedPreferences = getSharedPreferences("MyPreferences", MODE_PRIVATE)
        val usuarioId = sharedPreferences.getInt("USER_ID", -1)

        if (usuarioId != -1) {
            fetchArribos(usuarioId)
        } else {
            Toast.makeText(this, "Usuario no encontrado", Toast.LENGTH_LONG).show()
        }
    }

    private fun updateArribosView(arribos: List<Arribo>) {
        adapter = ArriboAdapter(arribos) { arribo ->
            // Maneja el clic en un arribo
            val intent = Intent(this, ArribosCapturaActivity::class.java).apply {
                putExtra("ARRIBO_ID", arribo.id) // Asegúrate de que `id` esté disponible en `Arribo`
            }
            startActivity(intent)
        }
        recyclerView.adapter = adapter
    }

    private fun fetchArribos(usuarioId: Int) {
        val apiService = RetrofitClient.instance.create(ApiService::class.java)
        apiService.getArribos(usuarioId).enqueue(object : Callback<List<Arribo>> {
            override fun onResponse(call: Call<List<Arribo>>, response: Response<List<Arribo>>) {
                if (response.isSuccessful) {
                    val arribos = response.body() ?: emptyList()
                    updateArribosView(arribos)
                }
            }

            override fun onFailure(call: Call<List<Arribo>>, t: Throwable) {
                Toast.makeText(this@ArriboActivity, "Error de red: ${t.message}", Toast.LENGTH_LONG).show()
            }
        })
    }
}
