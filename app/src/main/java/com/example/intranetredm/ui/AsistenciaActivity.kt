package com.example.intranetredm.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.intranetredm.R
import com.example.intranetredm.io.response.asistencia.AsistenciaClient
import com.example.intranetredm.io.response.asistencia.AsistenciaResponse
import com.example.intranetredm.model.asistencia.DatosEnvio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AsistenciaActivity : AppCompatActivity() {
    private lateinit var asistenciaClient: AsistenciaClient
    private lateinit var txthora: TextView
    private lateinit var txtfecha: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asistencia)

        asistenciaClient = AsistenciaClient.create()

        val btnEnviar = findViewById<Button>(R.id.btnGuardar)
        btnEnviar.setOnClickListener {
            enviarDatos()
        }

        txthora = findViewById(R.id.txtHora)
        txtfecha = findViewById(R.id.txtFecha)

        // Iniciar la actualización de fecha y hora
        updateDateTime()
    }

    private fun updateDateTime() {
        val calendar: Calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString: String = dateFormat.format(calendar.time)

        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeString: String = timeFormat.format(calendar.time)

        txtfecha.text = dateString
        txthora.text = timeString

        // Actualizar la fecha y hora cada segundo
        txtfecha.postDelayed({ updateDateTime() }, 1000)
    }

    private fun enviarDatos() {
        val idUsuarioEditText = findViewById<EditText>(R.id.editDocumento)
        val placaEditText = findViewById<EditText>(R.id.editPlaca)
        val tipoVehiculoEditText = findViewById<EditText>(R.id.editTipoVehiculo)
        val kilometrajeEditText = findViewById<EditText>(R.id.editKilometraje)

        val idUsuario = idUsuarioEditText.text.toString()
        val placa = placaEditText.text.toString()
        val tipoVehiculo = tipoVehiculoEditText.text.toString()
        val kilometraje = kilometrajeEditText.text.toString()

        // Validar que el campo de kilometraje no esté vacío
        if (kilometraje.isEmpty()) {
            Toast.makeText(applicationContext, "Ingrese el kilometraje", Toast.LENGTH_SHORT).show()
            return
        }

        val fecha = txtfecha.text.toString()
        val hora = txthora.text.toString()

        val datosEnvio = DatosEnvio(
            id_usuario = idUsuario,
            placa = placa,
            tipo_vehiculo = tipoVehiculo,
            kilometraje = kilometraje.toInt(),
            fecha = fecha,
            hora = hora
        )

        val call = asistenciaClient.enviarDatos(datosEnvio)

        call.enqueue(object : Callback<AsistenciaResponse> {
            override fun onResponse(call: Call<AsistenciaResponse>, response: Response<AsistenciaResponse>) {
                if (response.isSuccessful) {
                    val apiResponse = response.body()
                    if (apiResponse == null) {
                        Toast.makeText(applicationContext, "Error en el servidor", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (apiResponse.success) {
                        Toast.makeText(applicationContext, "Entrada registrada con exito", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@AsistenciaActivity, MenuActivity::class.java)
                        startActivity(intent)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(applicationContext, "Error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AsistenciaResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Error en la comunicación", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
