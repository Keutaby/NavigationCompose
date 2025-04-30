package com.example.camara_custom.pantallas

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import kotlinx.coroutines.scheduling.executor

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Composable
fun PantallaCamara() {
    val lente_a_usar = CameraSelector.LENS_FACING_BACK
    val  ciclo_de_vida_dueño = LocalLifecycleOwner.current

    val contexto = LocalContext.current

    val prevista = Preview.Builder().build()
    val vista_prevista = remember {
        PreviewView(contexto)
    }

    val camarax_selector = CameraSelector.Builder().requireLensFacing(lente_a_usar).build()

    val capturador_de_imagen = remember { ImageCapture.Builder().build() }

    //Filtros
    val analisis_de_imagen = remember {
        ImageAnalysis.Builder()
            .build()
            .also {
                it.setAnalyzer(executor) { imagen_proxy ->
                    // Aquí procesaremos cada fotograma
                    val bitmap = imagen_proxy.toBitmap() // Función de extensión para convertir ImageProxy a Bitmap
                    val bitmap_filtrado = aplicarFiltroGrises(bitmap)

                    // **Importante:** Necesitas mostrar `bitmap_filtrado` en tu `PreviewView`.
                    // `PreviewView` directamente no permite mostrar un Bitmap procesado.
                    // Una solución común es usar un `SurfaceTexture` y dibujar el Bitmap en un `Canvas`.
                    // Esto requiere una implementación más compleja que está fuera del alcance directo de esta respuesta inicial.

                    imagen_proxy.close() // ¡No olvides cerrar el ImageProxy después de usarlo!
                }
            }
    }

    LaunchedEffect(lente_a_usar) {
        val proveedor_local_camara = contexto.obtenerProveedorDeCamara()
        proveedor_local_camara.unbindAll()

        proveedor_local_camara.bindToLifecycle(ciclo_de_vida_dueño, camarax_selector, prevista, capturador_de_imagen)

        prevista.setSurfaceProvider(vista_prevista.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter){
        AndroidView(factory = {vista_prevista}, modifier = Modifier.fillMaxSize())

        Button(onClick = { tomar_foto(capturador_de_imagen, contexto) }) {
            Text("hola mundo")
        }
    }


}

private suspend fun Context.obtenerProveedorDeCamara(): ProcessCameraProvider =
    suspendCoroutine { continuacion ->
        ProcessCameraProvider.getInstance(this).also { proveedor_camara ->
            proveedor_camara.addListener({
                continuacion.resume(proveedor_camara.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }

private fun tomar_foto(capturador_imagen: ImageCapture, contexto: Context){
    val nombre_archivo = "CapturaFoto.jpeg"

    val valores_del_contenido = ContentValues().apply{
        put(MediaStore.MediaColumns.DISPLAY_NAME, nombre_archivo)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.P){
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/nuestra_app")
        }
    }

    val salida_foto = ImageCapture.OutputFileOptions.Builder(
        contexto.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI,//direccion de una locacion especifico que no es de archivo url
        valores_del_contenido
    ).build()

    capturador_imagen.takePicture(
        salida_foto,
        ContextCompat.getMainExecutor(contexto),
        object: ImageCapture.OnImageSavedCallback{
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.v("CAPTURA_EXITO", "Exito no ha pasado nada")
            }
            override fun onError(exception: ImageCaptureException) {
                Log.v("CAPTURA_ERROR", "Se identifico el siguiente error: ${exception.message}")
            }
        }
    )
}

//Filtros
fun ImageProxy.toBitmap(): Bitmap? {
    val image = this.image ?: return null
    val planes = image.planes
    val buffer = planes[0].buffer
    val pixelStride = planes[0].pixelStride
    val rowStride = planes[0].rowStride
    val rowPadding = rowStride - pixelStride * width
    val bitmap = Bitmap.createBitmap(
        width + rowPadding / pixelStride,
        height,
        Bitmap.Config.ARGB_8888
    )
    buffer.rewind()
    bitmap.copyPixelsFromBuffer(buffer)
    return bitmap
}
fun aplicarFiltroGrises(bitmapOriginal: Bitmap): Bitmap {
    val ancho = bitmapOriginal.width
    val alto = bitmapOriginal.height
    val bitmapGrises = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmapGrises)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f) // Establece la saturación a 0 para escala de grises
    val colorFilter = ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = colorFilter
    canvas.drawBitmap(bitmapOriginal, 0f, 0f, paint)
    return bitmapGrises
}
