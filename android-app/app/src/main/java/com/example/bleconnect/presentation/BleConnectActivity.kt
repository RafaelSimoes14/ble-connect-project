package com.example.bleconnect.presentation

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import org.koin.android.ext.android.get

class BleConnectActivity : ComponentActivity() {

    val viewModel: BleConnectViewModel by lazy { get() }

    private val blePermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            val allGranted = permissions.values.all { it }
            if (allGranted) {
                viewModel.connect()
            } else {
                viewModel.clearMessage()
            }
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            BleConnectScreen(
                viewModel = viewModel,
                onConnectClick = { requestBlePermissionsAndConnect() }
            )
        }
    }

    private fun requestBlePermissionsAndConnect() {
        val permissions = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            arrayOf(
                Manifest.permission.BLUETOOTH_SCAN,
                Manifest.permission.BLUETOOTH_CONNECT
            )
        } else {
            arrayOf(Manifest.permission.ACCESS_FINE_LOCATION)
        }

        val notGranted = permissions.filter { permission ->
            ContextCompat.checkSelfPermission(
                this,
                permission
            ) != PackageManager.PERMISSION_GRANTED
        }

        if (notGranted.isEmpty()) {
            viewModel.connect()
        } else {
            blePermissionLauncher.launch(notGranted.toTypedArray())
        }
    }
}