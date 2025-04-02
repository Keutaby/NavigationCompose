package com.example.clon_fulanito.API.SWAPI

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object InstanciaRetrofitSWAPI{
    private const val url_base = "https://swapi.dev/api/"

    private val servicio: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(url_base)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    // Lazy es un constructor que solo va a crear el objeto cuando sea solicitado y no desde un inicio. Para evitar tener
    //muchas cosas en la llamadas generales.
    val consumir_servicio: SWAPIinterfaz by lazy {
        servicio.create(SWAPIinterfaz::class.java)
    }
}