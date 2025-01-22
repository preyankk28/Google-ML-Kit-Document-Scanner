package com.example.invoicescanner.ui.features.scanDocument

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.invoicescanner.ui.features.scanDocument.view.ScanDocumentScreen
import com.example.invoicescanner.ui.features.scanDocument.viewModel.ScanDocumentViewModel
import com.example.invoicescanner.utils.getAllPermissions
import com.example.invoicescanner.utils.getCameraPermission
import com.example.invoicescanner.utils.getReadAndWriteStoragePermission
import com.example.invoicescanner.utils.toBitmap
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ScanDocumentActivity : ComponentActivity() {
    private lateinit var viewModel: ScanDocumentViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        if(!getAllPermissions()) getAllPermissions()
        viewModel = ViewModelProvider(this)[ScanDocumentViewModel::class.java]
        setContent {
            ScanDocumentScreen(
                viewModel = viewModel,
                onPickFromGallery = {
                    if (getReadAndWriteStoragePermission()) {
                        pickImageFromGallery.launch("image/*")
                    } else {
                        Toast.makeText(this, "Permission not found", Toast.LENGTH_SHORT).show()
                    }
                },
                onClickWithCamera = {
                    if (getCameraPermission()) {
                        captureImageWithCamera.launch(null)
                    } else {
                        Toast.makeText(this, "Permission not found", Toast.LENGTH_SHORT).show()
                    }
                },
            )
        }
    }

    private val pickImageFromGallery =
        registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
            uri?.let {
                val bitmap = it.toBitmap(context = this@ScanDocumentActivity)
                bitmap?.let { bmp ->
                    viewModel.updateImage(bitmap)
                    viewModel.processImage(bmp)
                }
            }
        }

    private val captureImageWithCamera =
        registerForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap: Bitmap? ->
            bitmap?.let {
                viewModel.updateImage(bitmap)
                viewModel.processImage(it)
            }
        }
}

