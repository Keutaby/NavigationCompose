package com.example.clon_fulanito.modelos.swapi

import android.util.Log

data class PaginaContenedora(
    val count: Int,
    val next: String?,
    val previous: String?,
    val results: List<NaveEspacial>
){
    fun indicar_pagina_siguiente(): Int?{
        Log.v("MODELO PAGINA", "Pagina extraida: ${extraer_pagina(next)}")
        return extraer_pagina(next)
    }

    fun indicar_pagina_anterior(): Int?{
        return extraer_pagina(previous)
    }

    fun extraer_pagina(pagina: String?): Int?{
        if(pagina != null){
            val resultado = pagina.split("?page=")
            return resultado[1].toInt()
        }

        return 0
    }
}