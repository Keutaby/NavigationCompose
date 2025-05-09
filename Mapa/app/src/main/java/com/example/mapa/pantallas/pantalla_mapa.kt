package com.example.mapa.pantallas

import android.location.Location
import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mapa.controlador.GPSControlador
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

@Composable
fun VistaMapa(controlador_gps: GPSControlador){
    val contexto = LocalContext.current
    val mapa_view = MapView(contexto)

    val ubicacion_actual: Location? by controlador_gps.ubicacion.observeAsState(initial = null)

    mapa_view.setTileSource(TileSourceFactory.MAPNIK)

    mapa_view.setBuiltInZoomControls(true) //si no esta en falso, nos deja botones feos y que se vea feo
    mapa_view.setMultiTouchControls(true)

    mapa_view.controller.setZoom(15.0)

    AndroidView(modifier = Modifier.fillMaxSize(),
        factory = { contexto ->
            mapa_view
        }){ mapa_view ->

        val marcador_ubicacion_actual = Marker(mapa_view)
        if(ubicacion_actual != null){
            marcador_ubicacion_actual.position = GeoPoint(ubicacion_actual!!) //tipo optional
            marcador_ubicacion_actual.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)

            mapa_view.controller.animateTo(marcador_ubicacion_actual.position) //to be shown a certain point
        }

        //val marcador = Marker(mapa_view) //donde se va a estar viendo
        //marcador.position = GeoPoint(31.71111510530462, -106.36503740849294) //puedes usar otras coordinadas, cowan pool
        //marcador.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        //marcador.icon

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

        //val distancia = parque_veterans.distanceToAsDouble(marcador.position) //calculate distance from one geoPoint to the other Geo point
        //Log.v("Distancia", "La distancia es ${distancia}")
        //mapa_view.controller.animateTo(parque_veterans)

        mapa_view.overlays.add(marcador_ubicacion_actual)
        //mapa_view.overlays.add(marcador)
        //mapa_view.overlays.add(marcador_2)
        //mapa_view.overlays.add(marcador_3)
    }
}