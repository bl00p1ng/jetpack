package com.unilibre.taller05.data.ml

import android.graphics.Bitmap
import androidx.camera.core.ImageProxy
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.label.ImageLabeling
import com.google.mlkit.vision.label.defaults.ImageLabelerOptions
import com.unilibre.taller05.domain.model.Ingrediente
import kotlinx.coroutines.suspendCancellableCoroutine
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException

@Singleton
class ImageLabelerHelper @Inject constructor() {

    private val labeler = ImageLabeling.getClient(
        ImageLabelerOptions.Builder()
            .setConfidenceThreshold(0.6f)
            .build()
    )

    suspend fun procesarBitmap(bitmap: Bitmap, rotation: Int = 0): List<Ingrediente> {
        val input = InputImage.fromBitmap(bitmap, rotation)
        return labelInternal(input)
    }

    suspend fun procesarImageProxy(proxy: ImageProxy): List<Ingrediente> {
        val media = proxy.image ?: return emptyList()
        val rotation = proxy.imageInfo.rotationDegrees
        val input = InputImage.fromMediaImage(media, rotation)
        return labelInternal(input)
    }

    private suspend fun labelInternal(input: InputImage): List<Ingrediente> =
        suspendCancellableCoroutine { cont ->
            labeler.process(input)
                .addOnSuccessListener { labels ->
                    val res = labels
                        .filter { it.confidence > 0.75f }
                        .map { Ingrediente(it.text, it.confidence) }
                    cont.resume(res)
                }
                .addOnFailureListener { cont.resumeWithException(it) }
        }
}
