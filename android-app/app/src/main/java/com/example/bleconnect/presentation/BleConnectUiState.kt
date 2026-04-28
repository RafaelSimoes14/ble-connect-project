package com.example.bleconnect.presentation

import com.example.shared.BleConnectionState

data class BleConnectUiState(
    val deviceName: String = "ESP32_BLE",
    val connectionState: BleConnectionState = BleConnectionState.Idle,
    val isRelay1On: Boolean = false,
    val isRelay2On: Boolean = false,
    val isLoading: Boolean = false,
    val message: String? = null
)