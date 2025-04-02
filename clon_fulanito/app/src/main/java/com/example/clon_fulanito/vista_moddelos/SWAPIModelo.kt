package com.example.clon_fulanito.vista_moddelos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clon_fulanito.API.SWAPI.RepositorioSWAPI
import com.example.clon_fulanito.modelos.swapi.PaginaContenedora
import kotlinx.coroutines.launch

class SWAPIModelo: ViewModel(){
    private val repositorio = RepositorioSWAPI()

    private val _pagina_actual = MutableLiveData<PaginaContenedora>()
    val pagina_actual: LiveData<PaginaContenedora> = _pagina_actual

    fun descargar_pagina(){
        viewModelScope.launch{
            try{
                val pagina = repositorio.obtener_naves_espaciales()
                _pagina_actual.value = pagina
            }
            catch(error: Exception){
                Log.v("DESCARGA DE PAGINA SWAPI", "${error.message}")
            }
        }
    }
}