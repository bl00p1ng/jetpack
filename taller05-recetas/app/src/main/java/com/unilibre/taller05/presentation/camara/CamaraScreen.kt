package com.unilibre.taller05.presentation.camara

import android.Manifest
import androidx.camera.core.CameraSelector
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.core.ImageProxy
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun CamaraScreen(
    onBack: () -> Unit,
    onIngredientesDetectados: () -> Unit,
    vm: CamaraViewModel = hiltViewModel()
) {
    val state by vm.state.collectAsStateWithLifecycle()
    val cameraPermission = rememberPermissionState(Manifest.permission.CAMERA)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Escanear ingredientes") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Atras")
                    }
                }
            )
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                cameraPermission.status.isGranted -> CameraContent(
                    procesando = state.procesando,
                    onCapture = { proxy -> vm.procesar(proxy, onIngredientesDetectados) }
                )
                else -> PermissionRequest(
                    rationale = "Necesitamos acceso a la camara para reconocer ingredientes.",
                    onRequest = { cameraPermission.launchPermissionRequest() }
                )
            }
            state.error?.let {
                Text(
                    "Error: $it",
                    modifier = Modifier.align(Alignment.BottomCenter).padding(16.dp),
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}

@Composable
private fun PermissionRequest(rationale: String, onRequest: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(rationale, style = MaterialTheme.typography.bodyLarge)
        Button(onClick = onRequest, modifier = Modifier.padding(top = 16.dp)) {
            Text("Otorgar permiso")
        }
    }
}

@Composable
private fun CameraContent(
    procesando: Boolean,
    onCapture: (ImageProxy) -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val previewView = remember { PreviewView(context) }
    val imageCapture = remember { ImageCapture.Builder().build() }

    AndroidView(
        modifier = Modifier.fillMaxSize(),
        factory = {
            val cameraProviderFuture = ProcessCameraProvider.getInstance(context)
            cameraProviderFuture.addListener({
                val cameraProvider = cameraProviderFuture.get()
                val preview = Preview.Builder().build().also {
                    it.setSurfaceProvider(previewView.surfaceProvider)
                }
                val selector = CameraSelector.DEFAULT_BACK_CAMERA
                try {
                    cameraProvider.unbindAll()
                    cameraProvider.bindToLifecycle(lifecycleOwner, selector, preview, imageCapture)
                } catch (_: Exception) {}
            }, ContextCompat.getMainExecutor(context))
            previewView
        }
    )

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
        if (procesando) {
            CircularProgressIndicator(modifier = Modifier.padding(32.dp))
        } else {
            FloatingActionButton(
                onClick = {
                    imageCapture.takePicture(
                        ContextCompat.getMainExecutor(context),
                        object : ImageCapture.OnImageCapturedCallback() {
                            override fun onCaptureSuccess(image: ImageProxy) {
                                onCapture(image)
                            }
                            override fun onError(exception: ImageCaptureException) {
                                image_dummy()
                            }
                        }
                    )
                },
                modifier = Modifier.padding(32.dp)
            ) {
                Icon(Icons.Default.PhotoCamera, contentDescription = "Capturar")
            }
        }
    }
}

private fun image_dummy() { /* no-op error swallow */ }
