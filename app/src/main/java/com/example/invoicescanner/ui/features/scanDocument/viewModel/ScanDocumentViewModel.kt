package com.example.invoicescanner.ui.features.scanDocument.viewModel

import android.graphics.Bitmap
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.TextRecognizer
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import dagger.hilt.android.lifecycle.HiltViewModel
import java.util.regex.Pattern
import javax.inject.Inject

@HiltViewModel
class ScanDocumentViewModel @Inject constructor() : ViewModel() {

    val invoiceCostResults: MutableLiveData<List<String>> = MutableLiveData(emptyList())
    private val _isProcessing = MutableLiveData(false)
    private val _selectedImage = MutableLiveData<Bitmap?>()
    private val _imageProcessedStatus = MutableLiveData<String?>()

    val isProcessing: LiveData<Boolean> get() = _isProcessing
    val selectedImage: LiveData<Bitmap?> get() = _selectedImage
    val imageProcessedStatus: LiveData<String?> get() = _imageProcessedStatus

    private val recognizer: TextRecognizer =
        TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    fun processImage(photoBitmap: Bitmap) {
        _isProcessing.value = true
        val image = InputImage.fromBitmap(photoBitmap, 0)

        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val costs = extractCosts(visionText.text)
                if (costs.isEmpty()) {
                    _imageProcessedStatus.postValue("No cost found in the document")
                } else {
                    _imageProcessedStatus.postValue("Document processed successfully")
                }
                invoiceCostResults.postValue(costs)
                _isProcessing.postValue(false)
            }
            .addOnFailureListener { exception ->
                _imageProcessedStatus.postValue("Failed to scan the document: ${exception.message}")
                _isProcessing.postValue(false)
            }
    }

    private fun extractCosts(text: String): List<String> {
        val costPattern = Pattern.compile("([$€£])?\\d+(\\.\\d{2})?")
        val matcher = costPattern.matcher(text)
        val costs = mutableListOf<String>()
        while (matcher.find()) {
            costs.add(matcher.group())
        }
        return costs
    }

    fun updateImage(bitmap: Bitmap) {
        _selectedImage.value = bitmap
    }
}
