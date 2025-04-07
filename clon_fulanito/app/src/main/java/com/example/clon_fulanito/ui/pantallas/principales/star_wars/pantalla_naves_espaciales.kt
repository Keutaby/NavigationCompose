package com.example.clon_fulanito.ui.pantallas.principales.star_wars

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import com.example.clon_fulanito.vista_moddelos.SWAPIModelo


@Composable
fun PantallaNavesEspaciales(modifier: Modifier, vm_swapi: SWAPIModelo){


    Log.v("Rcarga pantalla", "::${vm_swapi}::")

    val pagina_actual by vm_swapi.pagina_actual.observeAsState(null)

    LaunchedEffect(Unit) {
        vm_swapi.descargar_pagina(3)
    }

    Column(modifier = modifier){
        if(pagina_actual == null){
            Text("CARGANDO")
        }
        else{
            Text("Resultados")

            LazyColumn {

                items(pagina_actual!!.results){ nave_espacial ->
                    Text("Nave: ${nave_espacial.name}")
                    Text("Modelo: ${nave_espacial.model}")
                    HorizontalDivider()
                }
            }

            Row {
                Text("Pagina siguiente", modifier = Modifier.clickable {
                    Log.v("Rcarga funcion", "::${vm_swapi}::")
                        vm_swapi.pasar_a_siguiente_pagina()
                    }
                )

                Spacer(modifier = Modifier.fillMaxWidth(0.5f))

                Text("Pagina anterior", modifier = Modifier.clickable {
                    vm_swapi.pasar_a_anterior_pagina()
                }
                )
            }
        }
    }
}