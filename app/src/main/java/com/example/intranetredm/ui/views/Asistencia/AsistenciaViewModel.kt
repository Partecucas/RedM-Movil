package com.example.intranetredm.ui.views.Asistencia

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class AsistenciaViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "Asistencia"
    }
    val text: LiveData<String> = _text
}