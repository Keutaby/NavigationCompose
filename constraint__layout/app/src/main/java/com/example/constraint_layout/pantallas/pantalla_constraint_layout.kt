package com.example.constraint_layout.pantallas

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout

//https://developer.adnroid.com/develop/ui/compose/layouts/constraintlayout
//Informacion sobre el constraint layout

@Composable
fun PantallaDeCuadros(){
    ConstraintLayout(modifier = Modifier.fillMaxSize()) {
        val boton = createRef()
        val cajon_texto = createRef()

        Button(onClick = {}, modifier = Modifier.constrainAs(boton){
            top.linkTo(parent.top, margin = 16.dp)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
            bottom.linkTo(parent.bottom)
        }) {

        }

        Text("Pulsame", modifier = Modifier.constrainAs(cajon_texto){
            top.linkTo(boton.bottom)
            bottom.linkTo(parent.bottom)
            start.linkTo(parent.start)
            end.linkTo(parent.end)
        })
    }
}

@Preview(showBackground = true)
@Composable
fun Previsualizacion(){
    PantallaDeCuadros()
}