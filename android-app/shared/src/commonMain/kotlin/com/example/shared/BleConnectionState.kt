package com.example.shared

sealed interface BleConnectionState {
    object Idle : BleConnectionState
    object Scanning : BleConnectionState
    object Connecting : BleConnectionState
    data class Connected(val deviceName: String) : BleConnectionState
    object Disconnected : BleConnectionState
    data class Error(val message: String) : BleConnectionState
}