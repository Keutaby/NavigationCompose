package com.example.mapa.controlador

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class GPSControlador: ViewModel(){
    private val _autorizacion = MutableLiveData(false)
    val autorizacion: LiveData<Boolean> = _autorizacion //boolean for true or false

    private val _ubicacion = MutableLiveData<Location?>(null)
    val ubicacion: LiveData<Location?> = _ubicacion

    fun actualizar_ubicacion(nueva_ubicacion: Location?){
        _ubicacion.value = nueva_ubicacion
    }

    fun actualizar_permisos(tenemos_permisos: Boolean){
        _autorizacion.value = tenemos_permisos
    }
}