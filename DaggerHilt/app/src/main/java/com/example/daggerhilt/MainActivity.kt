package com.example.daggerhilt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.daggerhilt.ui.theme.DaggerHiltTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //primera entrada
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DaggerHiltTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column{
                        Greeting(
                            modifier = Modifier.padding(innerPadding)
                        )

                        Adios()
                    }

                }
            }
        }
    }
}

@Composable
fun Greeting(modifier: Modifier = Modifier, vm_publicaciones: VistaModeloPublicaciones = hiltViewModel()) {
    Text(
        text = "Hello ${vm_publicaciones.publicaciones.value}",
        modifier = modifier
    )
}

@Composable
fun Adios(vm_publicaciones: VistaModeloPublicaciones = hiltViewModel()){
    TextField(value = vm_publicaciones.publicaciones.value, onValueChange = {valor_nuevo -> vm_publicaciones.publicaciones.value = valor_nuevo})
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DaggerHiltTheme {
        Greeting()
    }
}