package com.example.shared

import com.juul.kable.Peripheral
import com.juul.kable.Scanner
import com.juul.kable.State
import com.juul.kable.WriteType
import com.juul.kable.characteristicOf
import com.juul.kable.peripheral
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class AndroidBleController : BleController {

    companion object {
        const val SERVICE_UUID = "0000FFE0-0000-1000-8000-00805F9B34FB"
        const val CHARACTERISTIC_UUID = "0000FFE1-0000-1000-8000-00805F9B34FB"
    }

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _connectionState =
        MutableStateFlow<BleConnectionState>(BleConnectionState.Idle)
    override val connectionState: StateFlow<BleConnectionState> = _connectionState

    private var peripheral: Peripheral? = null

    override suspend fun connect(deviceName: String) {
        try {
            _connectionState.value = BleConnectionState.Scanning

            val advertisement = Scanner()
                .advertisements
                .first { advertisement ->
                    advertisement.name?.contains(deviceName, ignoreCase = true) == true
                }

            _connectionState.value = BleConnectionState.Connecting

            val foundPeripheral = scope.peripheral(advertisement) {}
            peripheral = foundPeripheral

            observePeripheralState(foundPeripheral, deviceName)
            foundPeripheral.connect()
        } catch (exception: Exception) {
            _connectionState.value = BleConnectionState.Error(
                message = exception.message ?: "Erro ao conectar no dispositivo BLE"
            )
        }
    }

    override suspend fun disconnect() {
        peripheral?.disconnect()
        peripheral = null
        _connectionState.value = BleConnectionState.Disconnected
    }

    override suspend fun sendCommand(command: BleCommand) {
        val currentPeripheral = peripheral ?: return

        val characteristic = characteristicOf(
            service = SERVICE_UUID,
            characteristic = CHARACTERISTIC_UUID
        )

        currentPeripheral.write(
            characteristic = characteristic,
            data = command.payload,
            writeType = WriteType.WithResponse
        )
    }

    private fun observePeripheralState(
        peripheral: Peripheral,
        fallbackName: String
    ) {
        scope.launch {
            peripheral.state.collect { state ->
                _connectionState.value = when (state) {
                    is State.Connected -> {
                        BleConnectionState.Connected(
                            deviceName = peripheral.name ?: fallbackName
                        )
                    }

                    is State.Connecting.Bluetooth,
                    is State.Connecting.Services -> {
                        BleConnectionState.Connecting
                    }

                    is State.Disconnected -> {
                        BleConnectionState.Disconnected
                    }

                    else -> _connectionState.value
                }
            }
        }
    }
}