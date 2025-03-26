package com.example.clon_fulanito.vista_moddelos

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clon_fulanito.API.RepositorioFulanito
import com.example.clon_fulanito.modelos.Publicacion
import kotlinx.coroutines.launch

class FulanitoViewModel: ViewModel(){
    private val repositorio_de_datos = RepositorioFulanito()

    private val _publicaciones = MutableLiveData<List<Publicacion>>()
    val publicaciones: LiveData<List<Publicacion>> = _publicaciones

    fun descargar_todas_las_publicaciones(){
        //Aqui estan las corutinas
        viewModelScope.launch{
            try{
                val publicaciones_obtenidas = repositorio_de_datos.obtener_publicaciones()
                _publicaciones.value = publicaciones_obtenidas
            }
            catch(error: Exception){
                Log.v("DESCARGAR DE PUBLICACIONES", "${error.message}")
            }
        }
    }
}