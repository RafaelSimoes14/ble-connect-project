package com.example.shared.domain.usecase

import com.example.shared.domain.repository.BleConnectRepository

data class DisconnectBleDeviceUseCase(
    private val repository: BleConnectRepository
) {
    suspend operator fun invoke() {
        repository.disconnect()
    }
}