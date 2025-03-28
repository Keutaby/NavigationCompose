package com.example.clon_fulanito.ui.pantallas.principales

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.clon_fulanito.vista_moddelos.FulanitoViewModel

@Composable
fun PantallaDePublicacion(modifier: Modifier, vm_fulanito: FulanitoViewModel){

    val comentarios by vm_fulanito.comentarios_de_publicacion.observeAsState(emptyList())

    val publicacion by vm_fulanito.publicacion_seleccionada.observeAsState(null)

    if(publicacion != null){
        Column{
            Text("Titulo: ${publicacion!!.title})")
            Text("${publicacion!!.body}")

            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxSize()){ //helps scroll
                items(comentarios){ comentario ->

                    Text("Titulo: ${comentario.name}")
                    Text("Publicacion: ${comentario.body}")
                    HorizontalDivider()


                }
            }
        }
    }
}