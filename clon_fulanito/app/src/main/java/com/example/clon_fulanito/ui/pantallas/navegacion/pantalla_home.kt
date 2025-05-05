package com.example.clon_fulanito.ui.pantallas.navegacion

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Popup
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.clon_fulanito.ui.pantallas.PantallaNavegadora
import com.example.clon_fulanito.ui.pantallas.PantallaPerfil
import com.example.clon_fulanito.ui.pantallas.navegacion.controladores.BotonesInferioresNavegacion
import com.example.clon_fulanito.ui.pantallas.navegacion.controladores.PantallaMenuPrincipal
import com.example.clon_fulanito.ui.pantallas.principales.star_wars.PantallaNavesEspaciales
import com.example.clon_fulanito.vista_moddelos.FulanitoViewModel
import com.example.clon_fulanito.vista_moddelos.SWAPIModelo


@Composable
fun MenuPrincipal(modifier: Modifier){
    var pantalla_actual by remember {
        mutableStateOf(0)
    }

    val control_navegacion = rememberNavController()

    val vm_fulanito = FulanitoViewModel()
    val vm_swapi = SWAPIModelo()

    Scaffold(modifier = Modifier, bottomBar = {
        NavigationBar {
            BotonesInferioresNavegacion().botones_de_navegacion().forEachIndexed{indice, boton_de_navegacion ->
                NavigationBarItem(
                    selected = indice == pantalla_actual,
                    label = {
                        Text("${boton_de_navegacion.etiqueta}")
                    },
                    icon = {
                        Icon(boton_de_navegacion.icono, contentDescription = "${boton_de_navegacion.etiqueta}")
                    },
                    onClick = {
                        pantalla_actual = indice

                        control_navegacion.navigate(boton_de_navegacion.ruta){
                            popUpTo(control_navegacion.graph.startDestinationId){
                                saveState = true
                            }

                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
        }
    }) { innerPadding ->
        NavHost(navController = control_navegacion,
            startDestination = PantallaMenuPrincipal.Home.ruta,
            modifier = Modifier.padding(innerPadding)){

            composable(PantallaMenuPrincipal.Home.ruta) {
                PantallaNavegadora(modifier = Modifier.fillMaxSize(), vm_fulanito = vm_fulanito)
                //Text("Hola pantalla A")
            }

            composable(PantallaMenuPrincipal.StarWars.ruta) {
                PantallaNavesEspaciales(modifier, vm_swapi)
            }

            composable(PantallaMenuPrincipal.Perfil.ruta) {
                //Text("Hola de Perfil")
                PantallaPerfil(modifier = Modifier.fillMaxSize())
            }
        }

}
}

@Preview(showBackground = true)
@Composable
fun prevista(){
    MenuPrincipal(Modifier.fillMaxSize())
}

/*
Text("Hola mundo", modifier = Modifier.clickable{
    control_navegacion.navigate(PantallaMenuPrincipal.Home.ruta){
        popUpTo(control_navegacion.graph.startDestinationId){
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
})

Text("Swapi", modifier = Modifier.clickable{
    control_navegacion.navigate(PantallaMenuPrincipal.StarWars.ruta){
        popUpTo(control_navegacion.graph.startDestinationId){
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
})

Text("Perfil", modifier = Modifier.clickable{
    control_navegacion.navigate(PantallaMenuPrincipal.Perfil.ruta){
        popUpTo(control_navegacion.graph.startDestinationId){
            saveState = true
        }

        launchSingleTop = true
        restoreState = true
    }
})*/