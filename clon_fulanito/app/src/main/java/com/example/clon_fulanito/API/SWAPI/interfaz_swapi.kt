package com.example.clon_fulanito.API.SWAPI

import com.example.clon_fulanito.modelos.swapi.NaveEspacial
import com.example.clon_fulanito.modelos.swapi.PaginaContenedora
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SWAPIinterfaz{
    //https://swapi.dev/api/starships/?page=2
    @GET("starships") //Mas delante hacemos las modificaciones necesarios
    //starships/?page=2 -> use the word page
    suspend fun obtener_naves_espaciales(@Query("page")pagina: Int): PaginaContenedora

    @GET("starships/{id}")
    suspend fun obtener_nave(@Path("id") id: Int): NaveEspacial
}