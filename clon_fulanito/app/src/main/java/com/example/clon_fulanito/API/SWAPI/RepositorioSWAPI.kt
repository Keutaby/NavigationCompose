package com.example.clon_fulanito.API.SWAPI

import com.example.clon_fulanito.modelos.swapi.NaveEspacial
import com.example.clon_fulanito.modelos.swapi.PaginaContenedora

class RepositorioSWAPI{
    private val servicio_swapi = InstanciaRetrofitSWAPI.consumir_servicio

    suspend fun obtener_naves_espaciales(pagina: Int): PaginaContenedora{
        return servicio_swapi.obtener_naves_espaciales(pagina)
    }

    suspend fun obtener_nave(id: Int): NaveEspacial{
        return servicio_swapi.obtener_nave(id)
    }
}