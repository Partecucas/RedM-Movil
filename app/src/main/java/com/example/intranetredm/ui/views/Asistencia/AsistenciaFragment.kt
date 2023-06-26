package com.example.intranetredm.ui.views.Asistencia

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.intranetredm.R
import com.example.intranetredm.databinding.FragmentAsistenciaBinding
import com.example.intranetredm.io.response.asistencia.AsistenciaClient
import com.example.intranetredm.io.response.asistencia.AsistenciaResponse
import com.example.intranetredm.model.asistencia.DatosEnvio
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*

class AsistenciaFragment : Fragment() {

    private var _binding: FragmentAsistenciaBinding? = null
    private lateinit var asistenciaClient: AsistenciaClient
    private lateinit var txthora: TextView
    private lateinit var txtfecha: TextView

    // This property is only valid between onCreateView and onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val galleryViewModel = ViewModelProvider(this).get(AsistenciaViewModel::class.java)

        _binding = FragmentAsistenciaBinding.inflate(inflater, container, false)
        val root: View = binding.root

        asistenciaClient = AsistenciaClient.create()

        val btnEnviar = binding.btnGuardar
        btnEnviar.setOnClickListener {
            enviarDatos()
        }

        binding.radioGroupTipoVehiculo.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId == binding.radioButtonCarro.id) {
                binding.carro.visibility = View.VISIBLE
                binding.moto.visibility=View.GONE
            }else if (checkedId==binding.radioButtonMoto.id){
                binding.moto.visibility=View.VISIBLE
                binding.carro.visibility = View.GONE
            }
        }

        txthora = binding.txtHora
        txtfecha = binding.txtFecha

        // Iniciar la actualización de fecha y hora
        updateDateTime()

        return root
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
        val idUsuarioEditText = binding.editDocumento
        val placaEditText = binding.editPlaca
        val tipoVehiculoEditText = binding.editTipoVehiculo
        val kilometrajeEditText = binding.editKilometraje

        val idUsuario = idUsuarioEditText.text.toString()
        val placa = placaEditText.text.toString()
        val tipoVehiculo = tipoVehiculoEditText.text.toString()
        val kilometraje = kilometrajeEditText.text.toString()

        // Validar que el campo de kilometraje no esté vacío
        if (kilometraje.isEmpty()) {
            Toast.makeText(requireContext(), "Ingrese el kilometraje", Toast.LENGTH_SHORT).show()
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
                        Toast.makeText(requireContext(), "Error en el servidor", Toast.LENGTH_SHORT).show()
                        return
                    }
                    if (apiResponse.success) {
                        Toast.makeText(requireContext(), "Entrada registrada con éxito", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(requireContext(), "Error en la respuesta del servidor", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(requireContext(), "Error en el servidor", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<AsistenciaResponse>, t: Throwable) {
                Toast.makeText(requireContext(), "Error en la comunicación", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
