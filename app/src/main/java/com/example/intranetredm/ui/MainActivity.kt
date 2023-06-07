package com.example.intranetredm.ui
import ApiService
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.intranetredm.R
import com.example.intranetredm.io.response.LoginResponse
import com.example.intranetredm.model.LoginRequest
import com.example.intranetredm.ui.MenuActivity
import com.example.intranetredm.ui.RegisterActivity
import com.example.intranetredm.util.PreferenceHelper
import com.example.intranetredm.util.PreferenceHelper.get
import com.example.intranetredm.util.PreferenceHelper.set
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private val apiservice: ApiService by lazy {
        ApiService.create()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val preferences = PreferenceHelper.defaultPrefs(this)
        if (preferences["token", ""].contains("."))
            goToMenu()


        val btnGoMenu = findViewById<Button>(R.id.btn_go_to_menu)
        btnGoMenu.setOnClickListener {
            Toast.makeText(applicationContext, "", Toast.LENGTH_SHORT).show()
            performLogin()
        }
    }

    private fun goToRegister() {
        val i = Intent(this, RegisterActivity::class.java)
        startActivity(i)
    }

    private fun goToMenu() {
        val i = Intent(this, MenuActivity::class.java)
        startActivity(i)
        finish()
    }

    private fun createSessionPreference(token: String) {
        val preferences = PreferenceHelper.defaultPrefs(this)
        preferences["token"] = token
    }

    private fun performLogin() {
        val etEmail = findViewById<EditText>(R.id.et_email).text.toString()
        val etPassword = findViewById<EditText>(R.id.et_password).text.toString()

        val loginRequest = LoginRequest(etEmail, etPassword)
        val call = apiservice.postLogin(loginRequest)

        call.enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse == null) {
                        Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (loginResponse.success) {
                        createSessionPreference(loginResponse.token)
                        val userName = loginResponse.user?.name
                        val i = Intent(this@MainActivity, MenuActivity::class.java)
                        i.putExtra("userName",userName )
                        startActivity(i)
                        finish()
                    } else {
                        Toast.makeText(applicationContext, "Credenciales Incorrectas", Toast.LENGTH_SHORT).show()
                    }


                } else {
                    Toast.makeText(applicationContext, "Se produjo un error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                Toast.makeText(applicationContext, "Se produjo un error en la comunicación", Toast.LENGTH_SHORT).show()
            }
        })
    }
}
