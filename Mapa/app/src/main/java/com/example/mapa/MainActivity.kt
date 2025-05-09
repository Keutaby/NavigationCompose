package com.example.mapa

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import com.example.mapa.controlador.GPSControlador
import com.example.mapa.pantallas.VistaMapa
import com.example.mapa.sensor.ClienteGPS
import com.example.mapa.ui.theme.MapaTheme
import org.osmdroid.config.Configuration


class MainActivity : ComponentActivity() {
    private lateinit var gps_sensor: ClienteGPS //guardar para mas adelante
    private val controlador_gps: GPSControlador by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        gps_sensor = ClienteGPS(this)

        if(!consultar_permisos_gps()){
            requestPermissions(arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ), permisos_totales_gps)
        }
        else{
            controlador_gps.actualizar_permisos(true)
        }

        enableEdgeToEdge()
        setContent {
            val tenemo_permitido_usar_gps: Boolean by controlador_gps.autorizacion.observeAsState(initial = false)

            LaunchedEffect(tenemo_permitido_usar_gps){
                if(tenemo_permitido_usar_gps){
                    gps_sensor.configurar_actualizar_ubicacion(controlador_gps)
                }
            }

            MapaTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    VistaMapa(controlador_gps)
                }
            }
        }

        val contexto = applicationContext
        Configuration.getInstance().load(contexto, PreferenceManager.getDefaultSharedPreferences(contexto)) //download/import the osmDroid so it works, not android
        Configuration.getInstance().userAgentValue = "Mapa"
    }
    private fun consultar_permisos_gps(): Boolean{
        var permiso_fine_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
        var permiso_coarse_location = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED
        return permiso_fine_location || permiso_coarse_location
    }

    companion object{
        const val permisos_totales_gps = 123 //request code #
    }
}



@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MapaTheme {
        VistaMapa(controlador_gps = GPSControlador())
    }
}