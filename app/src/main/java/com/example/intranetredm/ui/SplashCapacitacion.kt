package com.example.intranetredm.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.intranetredm.R

class SplashCapacitacion : AppCompatActivity() {
    private val splashTimeout: Long = 5000

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_capacitacion)

        Handler().postDelayed({
            val intent = Intent(this, CapacitacionActivity ::class.java)
            startActivity(intent)
            finish()
        }, splashTimeout)
    }
}