package com.example.bleconnect.domain

import com.example.shared.BleConnectionState
import com.example.shared.domain.repository.BleConnectRepository
import com.example.shared.domain.usecase.ObserveBleConnectionStateUseCase
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ObserveBleConnectionStateUseCaseTest {
    private val repository: BleConnectRepository = mockk()
    private val useCase = ObserveBleConnectionStateUseCase(repository)

    @Test
    fun givenRepositoryWithIdleState_whenInvoked_thenReturnsRepositoryConnectionState() = runTest {
        val stateFlow = MutableStateFlow<BleConnectionState>(BleConnectionState.Idle)
        every { repository.connectionState } returns stateFlow

        val result = useCase()

        assertEquals(BleConnectionState.Idle, result.value)
    }

    @Test
    fun givenRepositoryStateFlow_whenStateChanges_thenReturnedFlowReflectsUpdates() = runTest {
        val stateFlow = MutableStateFlow<BleConnectionState>(BleConnectionState.Idle)
        every { repository.connectionState } returns stateFlow

        val result = useCase()

        stateFlow.value = BleConnectionState.Connected("ESP32_BLE")
        assertEquals(BleConnectionState.Connected("ESP32_BLE"), result.value)

        stateFlow.value = BleConnectionState.Disconnected
        assertEquals(BleConnectionState.Disconnected, result.value)
    }
}