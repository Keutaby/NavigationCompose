package com.example.clon_fulanito.API

import com.example.clon_fulanito.modelos.Publicacion

class RepositorioFulanito{
    private val servicio_api_jsonplaceholder = InstanciaRetrofitJSONplaceholder.consumir_servicio

    suspend fun obtener_publicaciones(): List<Publicacion>{
        return servicio_api_jsonplaceholder.obtener_publicaciones()
    }
}