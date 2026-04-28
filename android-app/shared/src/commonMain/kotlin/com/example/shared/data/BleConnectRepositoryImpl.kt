package com.example.shared.data

import com.example.shared.domain.model.RelayType
import com.example.shared.domain.repository.BleConnectRepository
import com.example.shared.BleCommand
import com.example.shared.BleConnectionState
import com.example.shared.BleController
import kotlinx.coroutines.flow.StateFlow

class BleConnectRepositoryImpl(
    private val bleController: BleController
) : BleConnectRepository {
    override val connectionState: StateFlow<BleConnectionState>
        get() = bleController.connectionState

    override suspend fun connect(deviceName: String) {
        bleController.connect(deviceName)
    }

    override suspend fun disconnect() {
        bleController.disconnect()
    }

    override suspend fun turnOnRelay(relayType: RelayType) {
        val command = when (relayType) {
            RelayType.RELAY_1 -> BleCommand.RELAY_ONE_ON
            RelayType.RELAY_2 -> BleCommand.RELAY_TWO_ON
        }
        bleController.sendCommand(command)
    }

    override suspend fun turnOffRelay(relayType: RelayType) {
        val command = when (relayType) {
            RelayType.RELAY_1 -> BleCommand.RELAY_ONE_OFF
            RelayType.RELAY_2 -> BleCommand.RELAY_TWO_OFF
        }
        bleController.sendCommand(command)
    }

}