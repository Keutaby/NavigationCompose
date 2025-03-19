package com.example.navigationcompose.control_navegacion

import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController

@Preview
@Composable
fun PantallaNavegador(){
    val control_de_navegacion = rememberNavController()

    NavHost(navController = control_de_navegacion, startDestination = Pantalla1){
        composable<Pantalla1>{
            PantallUnoVista(navegar_hacia_pantalla_dos = {
                control_de_navegacion.navigate()
            })
        }

        conposbale<Pantalla2>{
            PantallaDosVista()
        }

        conposbale<Pantalla3>{
            PantallaTresVista()
        }
    }
}
