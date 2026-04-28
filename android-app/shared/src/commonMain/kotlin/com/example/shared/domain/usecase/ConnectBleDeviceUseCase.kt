package com.example.shared.domain.usecase

import com.example.shared.domain.repository.BleConnectRepository

class ConnectBleDeviceUseCase(
    private val repository: BleConnectRepository
) {
    suspend operator fun invoke(deviceName: String) {
        repository.connect(deviceName)
    }
}