package com.example.shared.domain.usecase

import com.example.shared.domain.model.RelayType
import com.example.shared.domain.repository.BleConnectRepository

class SendRelayCommandUseCase(
    private val repository: BleConnectRepository
) {
    suspend operator fun invoke(
        relayType: RelayType,
        isCurrentlyOn: Boolean
    ): Boolean {
        if (isCurrentlyOn) {
            repository.turnOffRelay(relayType)
            return false
        }
        repository.turnOnRelay(relayType)
        return true
    }
}