package com.recursoconfiable.logisticabarrios

import android.content.Intent
import android.content.pm.PackageManager
import okhttp3.MultipartBody
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class ArribosCapturaActivity : AppCompatActivity() {
    private val REQUEST_IMAGE_CAPTURE = 1
    private val CAMERA_PERMISSION_REQUEST_CODE = 100

    private lateinit var imageViewMarchamos: ImageView
    private lateinit var imageViewPuertas: ImageView

    private var currentPhotoPath: String? = null
    private var selectedImageView: ImageView? = null
    private var uriMarchamo: Uri? = null
    private var uriPuertas: Uri? = null
    private var arriboId: Int = -1  // Declarar arriboId aquí

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_arribos_captura)

        val checkBoxMarchamoSi = findViewById<CheckBox>(R.id.CheckBoxMarchamoSi)
        val checkBoxMarchamoNo = findViewById<CheckBox>(R.id.CheckBoxMarchamoNo)

        checkBoxMarchamoSi.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBoxMarchamoNo.isChecked = false
            }
        }

        checkBoxMarchamoNo.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                checkBoxMarchamoSi.isChecked = false
            }
        }

        val nombreConductor = findViewById<EditText>(R.id.NombreConductor)
        val telefonoConductor = findViewById<EditText>(R.id.TelefonoConductor)
        val placaTracto = findViewById<EditText>(R.id.PlacaTracto)
        val cero = findViewById<EditText>(R.id.Cero)
        val marchamo = findViewById<EditText>(R.id.Marchamo)

        fun clearHintOnFocus(editText: EditText) {
            editText.setOnFocusChangeListener { v, hasFocus ->
                if (hasFocus) {
                    (v as EditText).text.clear()
                }
            }
        }

        clearHintOnFocus(nombreConductor)
        clearHintOnFocus(telefonoConductor)
        clearHintOnFocus(placaTracto)
        clearHintOnFocus(cero)
        clearHintOnFocus(marchamo)

        val textFolioValueArribo = findViewById<TextView>(R.id.textFolioValueArribo)
        val buttonMarchamos: Button = findViewById(R.id.ButtonMarchamos)
        val buttonFotoPuertas: Button = findViewById(R.id.ButtonFotoPuertas)
        val buttonGuardar: Button = findViewById(R.id.ButtonGuardar)

        imageViewMarchamos = findViewById(R.id.imageViewMarchamo)
        imageViewPuertas = findViewById(R.id.imageViewPuertas)

        buttonMarchamos.setOnClickListener {
            selectedImageView = imageViewMarchamos
            checkCameraPermission { dispatchTakePictureIntent() }
        }

        buttonFotoPuertas.setOnClickListener {
            selectedImageView = imageViewPuertas
            checkCameraPermission { dispatchTakePictureIntent() }
        }

        buttonGuardar.setOnClickListener {
            val nombre = nombreConductor.text.toString()
            val telefono = telefonoConductor.text.toString()
            val placa = placaTracto.text.toString()
            val ceroValue = cero.text.toString()
            val marchamoValue = marchamo.text.toString()
            val marchamoDanado = if (checkBoxMarchamoSi.isChecked) "Si" else "No"

            if (nombre.isNotEmpty() && telefono.isNotEmpty() && placa.isNotEmpty() && ceroValue.isNotEmpty() && marchamoValue.isNotEmpty()) {
                uploadEvidencias(nombre, telefono, placa, ceroValue, marchamoValue, marchamoDanado)
            } else {
                Toast.makeText(this, "Por favor, complete todos los campos.", Toast.LENGTH_SHORT).show()
            }
        }

        // Obtener arriboId del Intent y asignarlo a la variable de clase
        arriboId = intent.getIntExtra("ARRIBO_ID", -1)

        if (arriboId != -1) {
            val apiService: ApiService = RetrofitClient.apiService

            apiService.getArriboById(arriboId).enqueue(object : Callback<Arribo> {
                override fun onResponse(call: Call<Arribo>, response: Response<Arribo>) {
                    if (response.isSuccessful) {
                        val arribo = response.body()
                        arribo?.let {
                            textFolioValueArribo.text = it.folio
                        } ?: run {
                            textFolioValueArribo.text = "Folio no disponible"
                        }
                    } else {
                        Toast.makeText(this@ArribosCapturaActivity, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<Arribo>, t: Throwable) {
                    Toast.makeText(this@ArribosCapturaActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
                }
            })
        } else {
            Toast.makeText(this, "ID de arribo no válido", Toast.LENGTH_SHORT).show()
        }
    }

    private fun checkCameraPermission(action: () -> Unit) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA)
            != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CAMERA),
                CAMERA_PERMISSION_REQUEST_CODE)
        } else {
            action()
        }
    }

    @Throws(IOException::class)
    private fun createImageFile(): File {
        val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
        val storageDir: File = getExternalFilesDir(null)!!
        return File.createTempFile(
            "JPEG_${timeStamp}_", /* prefijo */
            ".jpg", /* sufijo */
            storageDir /* directorio */
        ).apply {
            currentPhotoPath = absolutePath
        }
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == CAMERA_PERMISSION_REQUEST_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                dispatchTakePictureIntent()
            } else {
                Toast.makeText(this, "Permiso de cámara denegado", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun dispatchTakePictureIntent() {
        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                try {
                    val photoFile = createImageFile()
                    val photoURI: Uri = FileProvider.getUriForFile(
                        this,
                        "${applicationContext.packageName}.fileprovider",
                        photoFile
                    )
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI)
                    currentPhotoPath = photoFile.absolutePath
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
                } catch (ex: IOException) {
                    Toast.makeText(this, "Error al crear el archivo de imagen", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == RESULT_OK && requestCode == REQUEST_IMAGE_CAPTURE) {
            currentPhotoPath?.let {
                val imageBitmap = BitmapFactory.decodeFile(it)
                selectedImageView?.setImageBitmap(imageBitmap)
                selectedImageView?.visibility = View.VISIBLE
                if (selectedImageView == imageViewMarchamos) {
                    uriMarchamo = Uri.fromFile(File(it))
                } else if (selectedImageView == imageViewPuertas) {
                    uriPuertas = Uri.fromFile(File(it))
                }
            }
        }
    }

    private fun uploadEvidencias(
        nombre: String,
        telefono: String,
        placa: String,
        ceroValue: String,
        marchamoValue: String,
        marchamoDañado: String
    ) {
        val apiService = RetrofitClient.apiServiceEvidencias

        // Asegúrate de que las URIs no sean nulas
        val fileUriMarchamo: MultipartBody.Part? = uriMarchamo?.let {
            val file = File(it.path!!)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photoMarchamo", file.name, requestFile)
        }

        val fileUriPuertas: MultipartBody.Part? = uriPuertas?.let {
            val file = File(it.path!!)
            val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
            MultipartBody.Part.createFormData("photoPuertas", file.name, requestFile)
        }


        // Convertir los valores a RequestBody
        val idArriboBody = arriboId.toString().toRequestBody("text/plain".toMediaTypeOrNull())
        val nombreBody = nombre.toRequestBody("text/plain".toMediaTypeOrNull())
        val telefonoBody = telefono.toRequestBody("text/plain".toMediaTypeOrNull())
        val placaBody = placa.toRequestBody("text/plain".toMediaTypeOrNull())
        val ceroBody = ceroValue.toRequestBody("text/plain".toMediaTypeOrNull())
        val marchamoBody = marchamoValue.toRequestBody("text/plain".toMediaTypeOrNull())
        val marchamoDañadoBody = marchamoDañado.toRequestBody("text/plain".toMediaTypeOrNull())

        val call = apiService.uploadEvidencia(
            idArriboBody,
            nombreBody,
            telefonoBody,
            placaBody,
            ceroBody,
            marchamoBody,
            marchamoDañadoBody,
            fileUriMarchamo,
            fileUriPuertas
        )

        call.enqueue(object : Callback<Void> { // Asegúrate de que Callback coincida con el tipo de retorno
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                if (response.isSuccessful) {
                    Toast.makeText(this@ArribosCapturaActivity, "Evidencia subida con éxito", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this@ArribosCapturaActivity, "Error al subir evidencia", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(this@ArribosCapturaActivity, "Error de red: ${t.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

}

