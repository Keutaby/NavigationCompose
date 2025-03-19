package com.example.navigationcompose.Pantallas

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment

@Composable
fun PantallaUnoVista(navegar_hacia_pantalla_dos: () -> Unit){
    Column(modifier = Modifier.fillMaxSize().background(Color.Cyan),
        horizontalAlignment = Alignment.CenterHorizontally){
        Text("Hola desde la pantalla uno")
        Spacer(modifieer = Modifier.weight)
        Button(onclick = navegar_hacia_pantalla_dos){
            Text("Ir a pantalla 2")
        }
    }
}
