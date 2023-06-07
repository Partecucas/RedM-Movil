package com.example.intranetredm.ui

import android.annotation.SuppressLint
import android.icu.text.SimpleDateFormat
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import com.example.intranetredm.R
import java.util.*

class AsistenciaActivity : AppCompatActivity() {
    private lateinit var handler: Handler
    private lateinit var timeTextView: TextView
    private lateinit var dateTextView: TextView

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asistencia)

        timeTextView = findViewById(R.id.txtHora)
        dateTextView = findViewById(R.id.txtFecha)

        // Inicializar el handler y llamar a la función para actualizar la hora y la fecha
        handler = Handler()
        updateDateTime()

        // Actualizar la hora y la fecha cada segundo (1000 milisegundos)
        handler.postDelayed(runnable, 1000)
    } override fun onDestroy() {
        super.onDestroy()
        // Detener la actualización cuando el Activity se destruya
        handler.removeCallbacks(runnable)
    }

    private fun updateDateTime() {
        val calendar: Calendar = Calendar.getInstance()

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        val dateString: String = dateFormat.format(calendar.time)

        val timeFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
        val timeString: String = timeFormat.format(calendar.time)

        dateTextView.text = dateString
        timeTextView.text = timeString
    }

    private val runnable: Runnable = object : Runnable {
        override fun run() {
            updateDateTime()
            // Actualizar la hora y la fecha cada segundo (1000 milisegundos)
            handler.postDelayed(this, 1000)
        }
    }
}

