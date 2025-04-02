package com.example.clon_fulanito.modelos.swapi

data class PaginaContenedora(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NaveEspacial>
)