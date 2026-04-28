package com.example.shared

import kotlinx.coroutines.flow.StateFlow

interface BleController {
    val connectionState: StateFlow<BleConnectionState>

    suspend fun connect(deviceName: String)
    suspend fun disconnect()
    suspend fun sendCommand(command: BleCommand)
}