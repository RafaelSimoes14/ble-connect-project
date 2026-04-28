package com.example.shared.domain.usecase

import com.example.shared.domain.repository.BleConnectRepository
import com.example.shared.BleConnectionState
import kotlinx.coroutines.flow.StateFlow

class ObserveBleConnectionStateUseCase (
    private val repository: BleConnectRepository
) {
    operator fun invoke(): StateFlow<BleConnectionState> = repository.connectionState
}