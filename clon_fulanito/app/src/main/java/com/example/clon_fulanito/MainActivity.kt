package com.example.clon_fulanito

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.clon_fulanito.ui.theme.Clon_fulanitoTheme
import com.example.clon_fulanito.vista_moddelos.FulanitoViewModel

class MainActivity : ComponentActivity() {
    private val modelo_app = FulanitoViewModel()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Clon_fulanitoTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    PantallaDePublicaciones(vm_fulanito = modelo_app)
                }
            }
        }
    }
}

@Composable
fun PantallaDePublicaciones(vm_fulanito: FulanitoViewModel){
    val publicaciones_descargadas by vm_fulanito.publicaciones.observeAsState(emptyList())
    LaunchedEffect(Unit) {
        vm_fulanito.descargar_todas_las_publicaciones()
    }

    Column{
        if(publicaciones_descargadas.isEmpty()){
            Text("Aqui deberia colocar una barra de cargando")
        }
        else{
            LazyColumn{
                items(publicaciones_descargadas){ publicacion ->
                    Text("Titulo: ${publicacion.title}")
                    Text("Publicacion: ${publicacion.body}")
                    HorizontalDivider()
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Clon_fulanitoTheme {
        //Greeting("Android")
    }
}