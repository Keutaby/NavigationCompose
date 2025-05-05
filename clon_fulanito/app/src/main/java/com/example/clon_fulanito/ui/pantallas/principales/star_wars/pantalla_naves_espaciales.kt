package com.example.clon_fulanito.ui.pantallas.principales.star_wars

import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorManager
import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.clon_fulanito.vista_moddelos.Estados
import com.example.clon_fulanito.vista_moddelos.SWAPIModelo
import kotlin.math.abs

@Composable
fun PantallaNavesEspaciales(modifier: Modifier, vm_swapi: SWAPIModelo){
    val pagina_actual by vm_swapi.pagina_actual.observeAsState(null)
    val estado_de_swapi by vm_swapi.estado_actual.observeAsState(Estados.cargando)
    val mensaje by vm_swapi.mensaje.observeAsState("")

    val contexto = LocalContext.current
    val contador = remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        vm_swapi.descargar_pagina(3)
    }

    DisposableEffect(Unit) {
        val manejador_de_sensor = contexto.getSystemService(Context.SENSOR_SERVICE) as SensorManager
        val agitamiento_sensor = manejador_de_sensor.getDefaultSensor(Sensor.TYPE_ACCELEROMETER)
        val sensibilidad = 20

        val escucha = object: android.hardware.SensorEventListener {
            override fun onSensorChanged(event: SensorEvent?) {
                if (event != null){
                    val eje_x = event.values[0]
                    val eje_y = event.values[1]
                    val eje_z = event.values[2]

                    val sumatoria_de_ejes = abs(eje_x) + abs(eje_y) + abs(eje_z)
                    if (sumatoria_de_ejes > sensibilidad){
                        Log.v("Deteccion de Terremotos", "Terremoto de ${sumatoria_de_ejes} grados total")
                        if(estado_de_swapi == Estados.error && contador.value > 15){
                            vm_swapi.indicar_un_problema()
                        }
                        contador.value = contador.value + 1
                    }
                }
            }

            override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
        }

        manejador_de_sensor.registerListener(escucha, agitamiento_sensor, SensorManager.SENSOR_DELAY_NORMAL)

        onDispose {
            manejador_de_sensor.unregisterListener(escucha)
        }

    }

    Column(modifier = modifier.padding(16.dp)) {
        when(estado_de_swapi){
            Estados.cargando -> {
                CircularProgressIndicator(
                    modifier = Modifier.fillMaxWidth(0.8f).padding(vertical = 16.dp),
                    color = Color.Yellow,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant
                )
            }
            Estados.mostrando_lista_de_naves -> {
                Text("Resultados ${contador.value}")

                LazyColumn {

                    items(pagina_actual!!.results){ nave_espacial ->
                        Text("Nave: ${nave_espacial.name}", color = Color.Cyan)
                        Text("Modelo: ${nave_espacial.model}", color = Color.LightGray)
                        HorizontalDivider(color = Color.DarkGray)
                    }
                }

                Row {
                    Text("Pagina siguiente",
                        modifier = Modifier.clickable {
                            Log.v("Recarga funcion", "::${vm_swapi}::")
                            vm_swapi.pasar_a_siguiente_pagina()
                        },
                        color = Color.Yellow
                    )

                    Spacer(modifier = Modifier.fillMaxWidth(0.5f))

                    Text("Pagina anterior",
                        modifier = Modifier.clickable {
                            vm_swapi.pasar_a_anterior_pagina()
                        },
                        color = Color.Yellow
                    )


                }
            }

            Estados.mostrnado_nave -> {}
            Estados.error -> {
                Text("Error. Parece que la ifnormacion no esta disponible por el momento")
                Text("informacion tecnica: ${mensaje}")
                Button({ vm_swapi.descargar_pagina() }) { Text("Recargar") }
            }

            Estados.registrar_frustracion -> {
                Text("Agitar tu telefono como rabieta no va a ayudar a resolver tu problema. ")
                Text("Tal vez agitarlo mas fuerte unas 500 veces arregle el problema")
                Text("Cantida de veces agitado: ${contador.value}")
            }
        }

    }
}

@Preview(showBackground = true)
@Composable
fun prevista(){
    PantallaNavesEspaciales(modifier = Modifier.fillMaxSize(), vm_swapi = SWAPIModelo())
}