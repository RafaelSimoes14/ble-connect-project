package com.example.bleconnect.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.shared.BleConnectionState
import com.example.shared.domain.model.RelayType
import com.example.shared.domain.usecase.ConnectBleDeviceUseCase
import com.example.shared.domain.usecase.DisconnectBleDeviceUseCase
import com.example.shared.domain.usecase.ObserveBleConnectionStateUseCase
import com.example.shared.domain.usecase.SendRelayCommandUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class BleConnectViewModel(
    private val connectBleDeviceUseCase: ConnectBleDeviceUseCase,
    private val disconnectBleDeviceUseCase: DisconnectBleDeviceUseCase,
    private val observeBleConnectionStateUseCase: ObserveBleConnectionStateUseCase,
    private val sendRelayCommandUseCase: SendRelayCommandUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(BleConnectUiState())
    val uiState: StateFlow<BleConnectUiState> = _uiState.asStateFlow()

    init {
        observeConnectionState()
    }

    fun onDeviceNameChanged(value: String) {
        _uiState.value = _uiState.value.copy(deviceName = value)
    }

    fun connect() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                message = null
            )
            runCatching {
                connectBleDeviceUseCase(_uiState.value.deviceName)
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    message = throwable.message ?: "Erro ao conectar"
                )
            }
        }
    }

    fun disconnect() {
        viewModelScope.launch {
            runCatching {
                disconnectBleDeviceUseCase()
            }.onSuccess {
                _uiState.value = _uiState.value.copy(
                    isRelay1On = false,
                    isRelay2On = false,
                    message = "Dispositivo desconectado"
                )
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    message = throwable.message ?: "Erro ao desconectar"
                )
            }
        }
    }

    fun onRelay1Clicked() {
        viewModelScope.launch {
            val current = _uiState.value.isRelay1On

            runCatching {
                sendRelayCommandUseCase(
                    relayType = RelayType.RELAY_1,
                    isCurrentlyOn = current
                )
            }.onSuccess { newState ->
                _uiState.value = _uiState.value.copy(
                    isRelay1On = newState,
                    message = if (newState) "Relé 1 ligado" else "Relé 1 desligado"
                )
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    message = throwable.message ?: "Erro ao alternar Relé 1"
                )
            }
        }
    }

    fun onRelay2Clicked() {
        viewModelScope.launch {
            val current = _uiState.value.isRelay2On

            runCatching {
                sendRelayCommandUseCase(
                    relayType = RelayType.RELAY_2,
                    isCurrentlyOn = current
                )
            }.onSuccess { newState ->
                _uiState.value = _uiState.value.copy(
                    isRelay2On = newState,
                    message = if (newState) "Relé 2 ligado" else "Relé 2 desligado"
                )
            }.onFailure { throwable ->
                _uiState.value = _uiState.value.copy(
                    message = throwable.message ?: "Erro ao alternar Relé 2"
                )
            }
        }
    }

    fun clearMessage() {
        _uiState.value = _uiState.value.copy(message = null)
    }

    private fun observeConnectionState() {
        viewModelScope.launch {
            observeBleConnectionStateUseCase().collect { state ->
                _uiState.value = _uiState.value.copy(
                    connectionState = state,
                    isLoading = state is BleConnectionState.Scanning ||
                            state is BleConnectionState.Connecting
                )
                if (state is BleConnectionState.Disconnected) {
                    _uiState.value = _uiState.value.copy(
                        isRelay1On = false,
                        isRelay2On = false
                    )
                }
            }
        }
    }
}