package com.example.camara_custom.pantallas

import android.content.ContentValues
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.graphics.Paint
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.min

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

    var filtro_seleccionado by remember { mutableStateOf("Ninguno") }
    val filtros_disponibles = listOf("Ninguno", "Desenfoque", "Invertir", "Blanco y Negro", "Contraste")

    var imagen_capturada by remember { mutableStateOf<Bitmap?>(null) }
    val executor: ExecutorService = remember { Executors.newSingleThreadExecutor() }

    var tomarFotoClick by remember { mutableStateOf(0) }

    LaunchedEffect(lente_a_usar) {
        val proveedor_local_camara = contexto.obtenerProveedorDeCamara()
        proveedor_local_camara.unbindAll()
        proveedor_local_camara.bindToLifecycle(ciclo_de_vida_dueño, camarax_selector, prevista, capturador_de_imagen)
        prevista.setSurfaceProvider(vista_prevista.surfaceProvider)
    }

    LaunchedEffect(tomarFotoClick) {
        if (tomarFotoClick > 0) {
            capturador_de_imagen.takePicture(
                ContextCompat.getMainExecutor(contexto),
                object : ImageCapture.OnImageCapturedCallback() {
                    override fun onCaptureSuccess(image: ImageProxy) {
                        Log.d("CAPTURA_EXITO", "Imagen capturada")
                        val bitmap = convertir_image_proxy_a_bitmap(image)
                        image.close()
                        val bitmap_filtrado = aplicar_filtro(bitmap, filtro_seleccionado, contexto)
                        imagen_capturada = bitmap_filtrado
                    }

                    override fun onError(exception: ImageCaptureException) {
                        Log.e("CAPTURA_ERROR", "Error al capturar la imagen")
                    }
                }
            )
        }
    }

    Box(contentAlignment = Alignment.BottomCenter, modifier = Modifier.fillMaxSize()) {
        AndroidView(factory = { vista_prevista }, modifier = Modifier.fillMaxSize())

        Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.padding(bottom = 16.dp)) {
            var mostrar_menu_filtro by remember { mutableStateOf(false) }
            Button(onClick = { mostrar_menu_filtro = true }) {
                Text("Seleccionar Filtro")
            }
            DropdownMenu(
                expanded = mostrar_menu_filtro,
                onDismissRequest = { mostrar_menu_filtro = false }
            ) {
                filtros_disponibles.forEach { filtro ->
                    DropdownMenuItem(text = { Text(filtro) }, onClick = { filtro_seleccionado = filtro; mostrar_menu_filtro = false })
                }
            }
            Text("Filtro seleccionado: ${filtro_seleccionado}")
            Spacer(modifier = Modifier.height(8.dp))
            Button(onClick = { tomarFotoClick++ }) {
                Text("Tomar Foto")
            }
        }

        if (imagen_capturada != null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Image(bitmap = imagen_capturada!!.asImageBitmap(), contentDescription = "Imagen capturada con filtro")
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Button(onClick = {
                        imagen_capturada?.let { bitmap ->
                            guardar_foto(contexto, bitmap, executor, capturador_de_imagen)
                            imagen_capturada = null
                        }
                    }) {
                        Text("Guardar Foto")
                    }
                    Button(onClick = { imagen_capturada = null }) {
                        Text("Retomar")
                    }
                }
            }
        }
    }
}

//Filtros
private fun aplicar_filtro(bitmap: Bitmap, filtroTipo: String, contexto: Context): Bitmap {
    Log.v("FILTRO", "Aplicando filtro")
    return when (filtroTipo) {
        "Desenfoque" -> aplicar_filtro_desenfoque_simple(bitmap, 5)
        "Invertir" -> aplicar_filtro_invertir(bitmap)
        "Blanco y Negro" -> aplicar_filtro_blanco_y_negro(bitmap)
        "Contraste" -> aplicar_filtro_contraste(bitmap, 2f)
        else -> bitmap
    }
}

private fun aplicar_filtro_desenfoque_simple(bitmap: Bitmap, radio: Int): Bitmap {
    val ancho = bitmap.width
    val alto = bitmap.height
    val bitmapDesenfocado = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
    val pixelsOriginal = IntArray(ancho * alto)
    bitmap.getPixels(pixelsOriginal, 0, ancho, 0, 0, ancho, alto)
    val pixelsDesenfocados = IntArray(ancho * alto)

    for (y in 0 until alto) {
        for (x in 0 until ancho) {
            var rSum = 0
            var gSum = 0
            var bSum = 0
            var count = 0

            for (i in -radio..radio) {
                for (j in -radio..radio) {
                    val xPos = min(ancho - 1, kotlin.math.max(0, x + i))
                    val yPos = min(alto - 1, kotlin.math.max(0, y + j))
                    val index = yPos * ancho + xPos
                    val pixel = pixelsOriginal[index]
                    rSum += (pixel and 0xFF0000) shr 16
                    gSum += (pixel and 0x00FF00) shr 8
                    bSum += (pixel and 0x0000FF) shr 0
                    count++
                }
            }

            val avgR = rSum / count
            val avgG = gSum / count
            val avgB = bSum / count
            pixelsDesenfocados[y * ancho + x] = (avgR shl 16) or (avgG shl 8) or avgB or (0xFF shl 24)
        }
    }

    bitmapDesenfocado.setPixels(pixelsDesenfocados, 0, ancho, 0, 0, ancho, alto)
    return bitmapDesenfocado
}

private fun aplicar_filtro_invertir(bitmap: Bitmap): Bitmap {
    val ancho = bitmap.width
    val alto = bitmap.height
    val bitmap_invertido = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
    val lienzo = Canvas(bitmap_invertido)
    val pintura = Paint()
    val matriz_color = ColorMatrix(
        floatArrayOf(
            -1f, 0f, 0f, 0f, 255f,
            0f, -1f, 0f, 0f, 255f,
            0f, 0f, -1f, 0f, 255f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    val filtro = ColorMatrixColorFilter(matriz_color)
    pintura.colorFilter = filtro
    lienzo.drawBitmap(bitmap, 0f, 0f, pintura)
    return bitmap_invertido
}

private fun aplicar_filtro_blanco_y_negro(bitmap: Bitmap): Bitmap {
    val ancho = bitmap.width
    val alto = bitmap.height
    val bitmap_byn = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
    val lienzo = Canvas(bitmap_byn)
    val pintura = Paint()
    val matriz_color = ColorMatrix()
    matriz_color.setSaturation(0f)
    val filtro = ColorMatrixColorFilter(matriz_color)
    pintura.colorFilter = filtro
    lienzo.drawBitmap(bitmap, 0f, 0f, pintura)
    return bitmap_byn
}

private fun aplicar_filtro_contraste(bitmap: Bitmap, contraste: Float): Bitmap {
    val ancho = bitmap.width
    val alto = bitmap.height
    val bitmap_contraste = Bitmap.createBitmap(ancho, alto, Bitmap.Config.ARGB_8888)
    val lienzo = Canvas(bitmap_contraste)
    val pintura = Paint()
    val matriz_color = ColorMatrix(
        floatArrayOf(
            contraste, 0f, 0f, 0f, 0f,
            0f, contraste, 0f, 0f, 0f,
            0f, 0f, contraste, 0f, 0f,
            0f, 0f, 0f, 1f, 0f
        )
    )
    val filtro = ColorMatrixColorFilter(matriz_color)
    pintura.colorFilter = filtro
    lienzo.drawBitmap(bitmap, 0f, 0f, pintura)
    return bitmap_contraste
}

private fun convertir_image_proxy_a_bitmap(image: ImageProxy): Bitmap {
    val buffer = image.planes[0].buffer
    buffer.rewind()
    val bytes = ByteArray(buffer.capacity())
    buffer.get(bytes)
    return BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
}

private fun guardar_foto(
    contexto: Context,
    bitmap: Bitmap,
    executor: ExecutorService,
    capturadorImagen: ImageCapture
) {
    val nombreArchivo = "nuestra_app.jpeg"
    val valoresContenido = ContentValues().apply {
        put(MediaStore.MediaColumns.DISPLAY_NAME, nombreArchivo)
        put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            put(MediaStore.Images.Media.RELATIVE_PATH, "Pictures/nuestra_app")
        }
    }

    val outputOptions = ImageCapture.OutputFileOptions.Builder(
        contexto.contentResolver,
        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, //direccion de una locacion especifico que no es de archivo url
        valoresContenido
    ).build()

    capturadorImagen.takePicture(
        outputOptions,
        executor,
        object : ImageCapture.OnImageSavedCallback {
            override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                Log.d("GUARDAR_EXITO", "Foto filtrada guardada")
            }

            override fun onError(exception: ImageCaptureException) {
                Log.e("GUARDAR_ERROR", "Error al guardar la foto filtrada")
            }
        }
    )
}

private suspend fun Context.obtenerProveedorDeCamara(): ProcessCameraProvider =
    suspendCoroutine { continuacion ->
        ProcessCameraProvider.getInstance(this).also { proveedor_local_camara ->
            proveedor_local_camara.addListener({
                continuacion.resume(proveedor_local_camara.get())
            }, ContextCompat.getMainExecutor(this))
        }
    }