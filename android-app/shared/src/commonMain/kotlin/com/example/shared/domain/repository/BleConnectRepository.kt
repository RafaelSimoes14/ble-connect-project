package com.example.shared.domain.repository

import com.example.shared.domain.model.RelayType
import com.example.shared.BleConnectionState
import kotlinx.coroutines.flow.StateFlow

interface BleConnectRepository {
    val connectionState: StateFlow<BleConnectionState>

    suspend fun connect(deviceName: String)
    suspend fun disconnect()
    suspend fun turnOnRelay(relayType: RelayType)
    suspend fun turnOffRelay(relayType: RelayType)
}