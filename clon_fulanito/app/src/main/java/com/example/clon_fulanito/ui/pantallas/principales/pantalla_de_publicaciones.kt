package com.example.clon_fulanito.ui.pantallas.principales

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.layout
import androidx.compose.ui.unit.dp
import com.example.clon_fulanito.vista_moddelos.FulanitoViewModel

//https://jsonplaceholder.typicode.com/
@Composable
fun PantallaDePublicaciones(modifier: Modifier, vm_fulanito: FulanitoViewModel, navegar_siguiente: () -> Unit){
    val publicaciones_descargadas by vm_fulanito.publicaciones.observeAsState(emptyList())



    LaunchedEffect(Unit) {
        vm_fulanito.descargar_todas_las_publicaciones()
    }

    Column(modifier = modifier.padding(16.dp)){

        if(publicaciones_descargadas.isEmpty()){
            //Text("Aqui deberia colocar una barra de cargando")
            CircularProgressIndicator(
                color = MaterialTheme.colorScheme.primary,
                strokeWidth = 4.dp
            )
        }
        else{
            LazyColumn(horizontalAlignment = Alignment.CenterHorizontally, modifier = modifier.fillMaxSize()){ //helps scroll
                items(publicaciones_descargadas){ publicacion ->
                    //Cuando se usa el .clickable de modifier, debe se el ultimo en la cadena dde Modifie
                    Column(modifier = Modifier.clickable {
                        vm_fulanito.seleccionar_publicacion(publicacion.id)
                        navegar_siguiente()
                    }.padding(15.dp).background(color = Color.LightGray)){
                        Text("Titulo: ${publicacion.title}", color = Color.Magenta)
                        Spacer(modifier = Modifier.height((4.dp)))
                        Text("Publicacion: ${publicacion.body}", color = Color.DarkGray)
                        Spacer(modifier = Modifier.height(8.dp))
                        HorizontalDivider(color = MaterialTheme.colorScheme.secondary)
                    }

                }
            }
        }

    }
}