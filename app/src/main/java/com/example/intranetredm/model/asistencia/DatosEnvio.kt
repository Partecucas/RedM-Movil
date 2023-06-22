package com.example.intranetredm.model.asistencia

data class DatosEnvio(
    val id_usuario: String,
    val placa: String,
    val tipo_vehiculo: String,
    val kilometraje: Int,
    val fecha: String,
    val hora: String
)
