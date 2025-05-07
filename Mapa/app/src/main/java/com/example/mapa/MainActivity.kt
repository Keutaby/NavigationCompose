package com.example.mapa

import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mapa.ui.theme.MapaTheme
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MapaTheme {
                Surface(modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background) {
                    VistaMapa()
                }
            }
        }

        val contexto = applicationContext
        Configuration.getInstance().load(contexto, PreferenceManager.getDefaultSharedPreferences(contexto)) //download/import the osmDroid so it works, not android
        Configuration.getInstance().userAgentValue = "Mapa"
    }
}

@Composable
fun VistaMapa(){
    val contexto = LocalContext.current
    val mapa_view = MapView(contexto)

    mapa_view.setTileSource(TileSourceFactory.MAPNIK)

    mapa_view.setBuiltInZoomControls(true) //si no esta en falso, nos deja botones feos y que se vea feo
    mapa_view.setMultiTouchControls(true)

    mapa_view.controller.setZoom(15.0)

    AndroidView(modifier = Modifier.fillMaxSize(),
        factory = { contexto ->
            mapa_view
        }){ mapa_view ->
        val marcador = Marker(mapa_view) //donde se va a estar viendo
        marcador.position = GeoPoint(31.71111510530462, -106.36503740849294) //puedes usar otras coordinadas, cowan pool
        marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marcador.icon

        val marcador_2 = Marker(mapa_view)
        val parque_veterans = GeoPoint(31.92019790477828, -106.42046109544629) //get from google maps, veterans pool
        marcador_2.position = parque_veterans
        marcador_2.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marcador_2.icon

        val marcador_3 = Marker(mapa_view)
        val parque_Eastside = GeoPoint(31.816170773485606, -106.53212165580622) //get from google maps, Westside Natatorium pool
        marcador_3.position = parque_Eastside
        marcador_3.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marcador_3.icon

        val distancia = parque_veterans.distanceToAsDouble(marcador.position) //calculate distance from one geoPoint to the other Geo point
        Log.v("Distancia", "La distancia es ${distancia}")
        mapa_view.controller.animateTo(parque_veterans)

        mapa_view.overlays.add(marcador)
        mapa_view.overlays.add(marcador_2)
        mapa_view.overlays.add(marcador_3)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MapaTheme {
        VistaMapa()
    }
}