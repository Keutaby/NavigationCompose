package com.example.camara_custom.pantallas

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
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
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.runtime.MutableState
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.mutableStateOf

@Composable
fun PantallaCamara() {
    val lente_a_usar = CameraSelector.LENS_FACING_BACK
    val ciclo_de_vida_dueño = LocalLifecycleOwner.current

    val contexto = LocalContext.current

    val prevista = Preview.Builder().build()
    val vista_prevista = remember {
        PreviewView(contexto)
    }

    val camarax_selector = CameraSelector.Builder().requireLensFacing(lente_a_usar).build()

    val capturador_de_imagen = remember { ImageCapture.Builder().build() }

    //Filtros
    var seleccion_filtro: MutableState<String> = remember { mutableStateOf("None") } //Seleccion
    val filtros_escoger = listOf("None", "Grayscale", "Sepia", "Blur") // Filtros


    LaunchedEffect(lente_a_usar) {
        val proveedor_local_camara = contexto.obtenerProveedorDeCamara()
        proveedor_local_camara.unbindAll()

        proveedor_local_camara.bindToLifecycle(ciclo_de_vida_dueño, camarax_selector, prevista, capturador_de_imagen)

        prevista.setSurfaceProvider(vista_prevista.surfaceProvider)
    }

    Box(contentAlignment = Alignment.BottomCenter){
        AndroidView(factory = {vista_prevista}, modifier = Modifier.fillMaxSize())

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 16.dp)) {
            DropdownMenu(
                expanded = false,
                onDismissRequest = {  }
            ) {
                filtros_escoger.forEach { filtro ->
                    DropdownMenuItem(text = { Text(filtro) }, onClick = { seleccion_filtro.value = filtro })
                }
            }
            Text("Filtro seleccionado: ${seleccion_filtro.value}")

            Spacer(modifier = Modifier.height(8.dp))

            Button(onClick = { tomar_foto(capturador_de_imagen, contexto, seleccion_filtro) }) {
                Text("Take Photo")
            }
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

private fun tomar_foto(capturador_imagen: ImageCapture, contexto: Context, seleccionFiltro: Any){
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
                outputFileResults.savedUri?.let { uri ->
                    Log.v("CAPTURA_EXITO", "Exito no ha pasado nada")
                    aplicarFiltro(contexto, uri, seleccionFiltro.toString())
                }
            }
            override fun onError(exception: ImageCaptureException) {
                Log.v("CAPTURA_ERROR", "Se identifico el siguiente error: ${exception.message}")
            }
        }
    )
}

//Filtros
fun aplicarFiltro(context: Context, imageUri: android.net.Uri, filterType: String){
    Log.v("FILTRO", "Aplicando filtro")

    try {
        val inputStream = context.contentResolver.openInputStream(imageUri)
        val originalBitmap = BitmapFactory.decodeStream(inputStream)
        inputStream?.close()

        if (originalBitmap != null) {
            val filteredBitmap = when (filterType) {
                "Grayscale" -> applyGrayscaleFilter(originalBitmap)
                "Sepia" -> applySepiaFilter(originalBitmap)
                "Blur" -> applyBlurFilter(context, originalBitmap) // You'll need to implement this
                else -> originalBitmap // No filter
            }


            val outputStream = context.contentResolver.openOutputStream(imageUri)
            if (outputStream != null) {
                filteredBitmap.compress(Bitmap.CompressFormat.JPEG, 90, outputStream)
            }
            outputStream?.close()

            Log.d("FILTRO", "Filtro aplicado y guardado")
        } else {
            Log.e("FILTRO", "Error no se puede agregar el filtro")
        }
    } catch (e: Exception) {
        Log.e("FILTRO", "Error applying filter", e)
    }
}

//Filtro 1
fun applyGrayscaleFilter(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val grayscaleBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(grayscaleBitmap)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.setSaturation(0f)
    val filter = ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = filter
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return grayscaleBitmap
}

//Filtro 2
fun applySepiaFilter(bitmap: Bitmap): Bitmap {
    val width = bitmap.width
    val height = bitmap.height
    val sepiaBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(sepiaBitmap)
    val paint = Paint()
    val colorMatrix = ColorMatrix()
    colorMatrix.set(
        floatArrayOf(
            0.393f, 0.769f, 0.189f, 0f, 0f,
            0.349f, 0.686f, 0.168f, 0f, 0f,
            0.272f, 0.534f, 0.131f, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    val filter = ColorMatrixColorFilter(colorMatrix)
    paint.colorFilter = filter
    canvas.drawBitmap(bitmap, 0f, 0f, paint)
    return sepiaBitmap
}

//Filtro 3
fun applyBlurFilter(context: Context, bitmap: Bitmap): Bitmap {
    // Implement your blur filter logic here (e.g., using RenderScript or a library)
    Log.w("FILTRO", "Blur filter not yet implemented")
    return bitmap
}