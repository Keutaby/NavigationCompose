package com.example.clon_fulanito.API

import com.example.clon_fulanito.modelos.Publicacion
import retrofit2.http.GET

interface FulanitoAPIServicio{
    //Cuando se use la respuesta de internet se resolvera como : https://jsonplaceholder.typicode.com/posts
    @GET("/posts")
    suspend fun obtener_publicaciones(): List<Publicacion>
}