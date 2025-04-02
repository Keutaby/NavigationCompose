package com.example.clon_fulanito.modelos.swapi

data class NaveEspacial(
    val name: String,
    val model: String,
    val manufacturer: String,
    val cost_in_credits: String,
    val length: String,
    val max_atmosphering_speed: String,
    val crew: String,
    val passengers: String,
    val cargo_capacity: String,
    val consumables: String,
    val hyperdrive_rating: String,
    val MGLT: String,
    val starship_class: String,
    val pilots: List<String>, //Lista de personas que han usado la nave como pilotos
    val films: List<String>, //Lista de peliculas donde aparece
    val created: String,
    val edited: String,
    val url: String
){
    fun sacar_id_nave(): Int{
        TODO()
        return -1
    }

    fun sacar_lista_peliculas(): List<Int>{
        TODO()
    }
    fun scar_lista_pilotos(): List<Int>{
        TODO()
    }
}
