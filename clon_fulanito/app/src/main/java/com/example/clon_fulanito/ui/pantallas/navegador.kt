package com.example.clon_fulanito.ui.pantallas

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clon_fulanito.ui.pantallas.principales.PantallaDePublicacion
import com.example.clon_fulanito.ui.pantallas.principales.PantallaDePublicaciones
import com.example.clon_fulanito.vista_moddelos.FulanitoViewModel


@Composable
fun PantallaNavegadora(modifier: Modifier, vm_fulanito: FulanitoViewModel){
    val control_de_navegacion = rememberNavController()

    NavHost(navController = control_de_navegacion, startDestination = PantallaPublicaciones){
        composable<PantallaPublicaciones> {
            PantallaDePublicaciones(modifier, vm_fulanito, navegar_siguiente = {
                control_de_navegacion.navigate(PantallaPublicacion)
            })
        }

        composable<PantallaPublicacion> {
            PantallaDePublicacion(modifier = modifier, vm_fulanito)
        }

        composable<PantallaPerfil> {  }
    }
}