package com.example.intranetredm.ui
import ApiService
import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.intranetredm.R
import com.example.intranetredm.util.PreferenceHelper
import com.example.intranetredm.util.PreferenceHelper.set
import com.example.intranetredm.util.PreferenceHelper.get
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MenuActivity : AppCompatActivity() {
    private val apiservice by lazy {
        ApiService.create()
    }
    private val preferences by lazy {
        PreferenceHelper.defaultPrefs(this)
    }
    private val handler = Handler()
    private val logoutRunnable = Runnable {
        perfomLogout()
    }

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)
        val userName = intent.getStringExtra("userName")
        val tvUserName = findViewById<TextView>(R.id.textView)
        tvUserName.text = userName


        val btnAsistencia = findViewById<Button>(R.id.btn_go_to_asistencia)
        btnAsistencia.setOnClickListener {
            goToAsistencia()
        }

        val btnLogout = findViewById<Button>(R.id.btn_logout)
        btnLogout.setOnClickListener {
            perfomLogout()
        }

        // Ejecutar el logout automáticamente después de 1 minuto
        handler.postDelayed(logoutRunnable, 4000000)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Cancelar la ejecución del logout si el Activity se destruye antes de 1 minuto
        handler.removeCallbacks(logoutRunnable)
    }

    private fun goToAsistencia() {
        val i = Intent(this, AsistenciaActivity::class.java)
        startActivity(i)
    }

    private fun goToLogin() {
        val i = Intent(this, MainActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun perfomLogout() {
        val token = preferences["token", ""]
        val call = apiservice.postLogout("Bearer $token")
        call.enqueue(object : Callback<Void> {
            override fun onResponse(call: Call<Void>, response: Response<Void>) {
                clearSessionPreference()
                goToLogin()
            }

            override fun onFailure(call: Call<Void>, t: Throwable) {
                Toast.makeText(
                    applicationContext,
                    "Se produjo un error en el servidor",
                    Toast.LENGTH_SHORT
                ).show()
            }

        })
    }

    private fun clearSessionPreference() {
        preferences["token"] = ""
    }
}
