package com.example.intranetredm.io.response.asistencia

import com.example.intranetredm.model.asistencia.DatosEnvio
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface AsistenciaClient {
    @POST("entrada")  // Reemplaza "ruta_de_tu_endpoint" con la ruta real en tu API
    fun enviarDatos(@Body datos: DatosEnvio): Call<AsistenciaResponse>  // Reemplaza ApiResponse con el nombre de tu clase de respuesta

    companion object Factory {
        private const val BASE_URL = "http://192.168.101.76:3000/"

        fun create(): AsistenciaClient {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(AsistenciaClient::class.java)
        }
    }
}
