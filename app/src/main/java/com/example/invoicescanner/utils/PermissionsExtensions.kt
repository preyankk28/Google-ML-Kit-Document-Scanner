package com.example.invoicescanner.utils

import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

fun Activity.getAllPermissions(): Boolean {
    val permissions = mutableListOf<String>()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(android.Manifest.permission.READ_MEDIA_IMAGES)
        permissions.add(android.Manifest.permission.CAMERA)
    } else {
        permissions.add(android.Manifest.permission.CAMERA)
        permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    if (permissions.isEmpty()) {
        return false
    }

    val listPermissionsNeeded = permissions.filter {
        ContextCompat.checkSelfPermission(
            this,
            it
        ) != PackageManager.PERMISSION_GRANTED
    }

    if (listPermissionsNeeded.isNotEmpty()) {
        ActivityCompat.requestPermissions(
            this,
            listPermissionsNeeded.toTypedArray(),
            101
        )
        return false
    }
    return true
}

fun Activity.getReadAndWriteStoragePermission(): Boolean {
    val permissions = mutableListOf<String>()

    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        permissions.add(android.Manifest.permission.READ_MEDIA_IMAGES)
        permissions.add(android.Manifest.permission.CAMERA)
    } else {
        permissions.add(android.Manifest.permission.CAMERA)
        permissions.add(android.Manifest.permission.READ_EXTERNAL_STORAGE)
        permissions.add(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)
    }

    if (permissions.isEmpty()) {
        return false
    }

    val listPermissionsNeeded = permissions.filter {
        ContextCompat.checkSelfPermission(
            this,
            it
        ) != PackageManager.PERMISSION_GRANTED
    }

    if (listPermissionsNeeded.isNotEmpty()) {
        return false
    }
    return true
}


fun Activity.getCameraPermission(): Boolean {
    val permissions = android.Manifest.permission.CAMERA

    val listPermissionsNeeded = ContextCompat.checkSelfPermission(
        this,
        permissions
    ) != PackageManager.PERMISSION_GRANTED

    return !listPermissionsNeeded
}