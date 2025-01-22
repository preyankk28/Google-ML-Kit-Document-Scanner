package com.example.invoicescanner.ui.features.scanDocument.view

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.invoicescanner.ui.features.scanDocument.viewModel.ScanDocumentViewModel

@Composable
fun ScanDocumentScreen(
    onPickFromGallery: () -> Unit,
    onClickWithCamera: () -> Unit,
    viewModel: ScanDocumentViewModel,
) {
    var showFullImageDialog by remember { mutableStateOf(false) }
    val costs by viewModel.invoiceCostResults.observeAsState(emptyList())
    val bitmap by viewModel.selectedImage.observeAsState()
    val isProcessing by viewModel.isProcessing.observeAsState(false)
    val imageProcessedStatus by viewModel.imageProcessedStatus.observeAsState()

    Scaffold { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(color = Color(0xFFF4F6F9)),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            item {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .padding(top = 24.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Button(
                        onClick = onPickFromGallery,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6200EE))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Pick from Gallery",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Pick from Gallery",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Button(
                        onClick = onClickWithCamera,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF03DAC5))
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = "Click with Camera",
                            tint = Color.White
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Click with Camera",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                        )
                    }
                }
            }

            if (isProcessing) {
                item {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .padding(16.dp),
                        color = Color(0xFF6200EE)
                    )
                }
            } else {
                bitmap?.let {
                    item {
                        Spacer(modifier = Modifier.height(32.dp))
                        Image(
                            bitmap = it.asImageBitmap(),
                            contentDescription = "Captured Image",
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .padding(16.dp)
                                .clip(RoundedCornerShape(16.dp))
                                .background(Color.Gray)
                                .clickable {
                                    showFullImageDialog = true
                                }
                        )
                    }
                }
                imageProcessedStatus?.let { status ->
                    item {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = status,
                            color = if (status.contains("Failed")) Color.Red else Color.Green,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (showFullImageDialog && bitmap != null) {
                item {
                    FullScreenImageDialog(
                        bitmap = bitmap!!,
                        onDismiss = { showFullImageDialog = false })
                }
            }

            if (costs.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(32.dp))
                    Text(
                        text = "Detected Costs:",
                        color = Color(0xFF333333),
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                    )
                }
                items(costs) { cost ->
                    Text(
                        text = cost,
                        color = Color(0xFF333333),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier
                            .padding(vertical = 8.dp, horizontal = 16.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun FullScreenImageDialog(bitmap: Bitmap, onDismiss: () -> Unit) {
    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color(0xCC000000))
                .clickable { onDismiss() }
        ) {
            Image(
                bitmap = bitmap.asImageBitmap(),
                contentDescription = "Full Screen Image",
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
                    .padding(32.dp)
            )
        }
    }
}

