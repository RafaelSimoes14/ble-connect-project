package com.example.bleconnect.presentation

import com.example.shared.BleConnectionState
import com.example.shared.domain.model.RelayType
import com.example.shared.domain.usecase.ConnectBleDeviceUseCase
import com.example.shared.domain.usecase.DisconnectBleDeviceUseCase
import com.example.shared.domain.usecase.ObserveBleConnectionStateUseCase
import com.example.shared.domain.usecase.SendRelayCommandUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertNull
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class BleConnectViewModelTest {
    private val connectUseCase: ConnectBleDeviceUseCase = mockk()
    private val disconnectUseCase: DisconnectBleDeviceUseCase = mockk()
    private val observeUseCase: ObserveBleConnectionStateUseCase = mockk()
    private val sendRelayUseCase: SendRelayCommandUseCase = mockk()

    private val connectionStateFlow = MutableStateFlow<BleConnectionState>(BleConnectionState.Idle)
    private val testDispatcher = StandardTestDispatcher()

    private lateinit var viewModel: BleConnectViewModel

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        every { observeUseCase() } returns connectionStateFlow
        viewModel = BleConnectViewModel(
            connectUseCase,
            disconnectUseCase,
            observeUseCase,
            sendRelayUseCase
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun givenNewViewModel_whenInitialized_thenDefaultUiStateIsSet() {
        assertEquals("ESP32_BLE", viewModel.uiState.value.deviceName)
        assertFalse(viewModel.uiState.value.isLoading)
        assertFalse(viewModel.uiState.value.isRelay1On)
        assertFalse(viewModel.uiState.value.isRelay2On)
        assertNull(viewModel.uiState.value.message)
        assertEquals(BleConnectionState.Idle, viewModel.uiState.value.connectionState)
    }

    @Test
    fun givenViewModel_whenOnDeviceNameChanged_thenDeviceNameIsUpdatedInState() {
        viewModel.onDeviceNameChanged("MyDevice")
        assertEquals("MyDevice", viewModel.uiState.value.deviceName)
    }

    @Test
    fun givenMessageSet_whenClearMessageCalled_thenMessageIsNull() = runTest(testDispatcher) {
        coEvery { connectUseCase(any()) } throws RuntimeException("error")
        viewModel.connect()
        advanceUntilIdle()

        viewModel.clearMessage()

        assertNull(viewModel.uiState.value.message)
    }

    @Test
    fun givenValidDeviceName_whenConnectCalled_thenConnectUseCaseIsInvokedWithDeviceName() =
        runTest(testDispatcher) {
            coEvery { connectUseCase(any()) } returns Unit

            viewModel.connect()
            advanceUntilIdle()

            coVerify { connectUseCase("ESP32_BLE") }
        }

    @Test
    fun givenConnectUseCaseThrows_whenConnectCalled_thenErrorMessageIsSetAndIsLoadingIsFalse() =
        runTest(testDispatcher) {
            coEvery { connectUseCase(any()) } throws RuntimeException("Device not found")

            viewModel.connect()
            advanceUntilIdle()

            assertEquals("Device not found", viewModel.uiState.value.message)
            assertFalse(viewModel.uiState.value.isLoading)
        }

    @Test
    fun givenConnectUseCaseThrowsWithNoMessage_whenConnectCalled_thenFallbackErrorMessageIsSet() =
        runTest(testDispatcher) {
            coEvery { connectUseCase(any()) } throws RuntimeException()

            viewModel.connect()
            advanceUntilIdle()

            assertEquals("Erro ao conectar", viewModel.uiState.value.message)
        }

    @Test
    fun givenConnectedDevice_whenDisconnectSucceeds_thenRelaysAreResetAndMessageIsSet() =
        runTest(testDispatcher) {
            coEvery { disconnectUseCase() } returns Unit

            viewModel.disconnect()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isRelay1On)
            assertFalse(viewModel.uiState.value.isRelay2On)
            assertEquals("Dispositivo desconectado", viewModel.uiState.value.message)
        }

    @Test
    fun givenDisconnectUseCaseThrows_whenDisconnectCalled_thenErrorMessageIsSet() =
        runTest(testDispatcher) {
            coEvery { disconnectUseCase() } throws RuntimeException("BLE failure")

            viewModel.disconnect()
            advanceUntilIdle()

            assertEquals("BLE failure", viewModel.uiState.value.message)
        }

    @Test
    fun givenDisconnectUseCaseThrowsWithNoMessage_whenDisconnectCalled_thenFallbackErrorMessageIsSet() =
        runTest(testDispatcher) {
            coEvery { disconnectUseCase() } throws RuntimeException()

            viewModel.disconnect()
            advanceUntilIdle()

            assertEquals("Erro ao desconectar", viewModel.uiState.value.message)
        }

    @Test
    fun givenRelay1IsOff_whenOnRelay1Clicked_thenRelay1IsTurnedOnAndMessageIsSet() =
        runTest(testDispatcher) {
            coEvery { sendRelayUseCase(RelayType.RELAY_1, false) } returns true

            viewModel.onRelay1Clicked()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isRelay1On)
            assertEquals("Relé 1 ligado", viewModel.uiState.value.message)
        }

    @Test
    fun givenRelay1IsOn_whenOnRelay1Clicked_thenRelay1IsTurnedOffAndMessageIsSet() =
        runTest(testDispatcher) {
            coEvery { sendRelayUseCase(RelayType.RELAY_1, false) } returns true
            viewModel.onRelay1Clicked()
            advanceUntilIdle()

            coEvery { sendRelayUseCase(RelayType.RELAY_1, true) } returns false
            viewModel.onRelay1Clicked()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isRelay1On)
            assertEquals("Relé 1 desligado", viewModel.uiState.value.message)
        }

    @Test
    fun givenSendRelayCommandThrows_whenOnRelay1Clicked_thenErrorMessageIsSetAndStateIsUnchanged() =
        runTest(testDispatcher) {
            coEvery {
                sendRelayUseCase(
                    RelayType.RELAY_1,
                    false
                )
            } throws RuntimeException("Relay failure")

            viewModel.onRelay1Clicked()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isRelay1On)
            assertEquals("Relay failure", viewModel.uiState.value.message)
        }

    @Test
    fun givenRelay2IsOff_whenOnRelay2Clicked_thenRelay2IsTurnedOnAndMessageIsSet() =
        runTest(testDispatcher) {
            coEvery { sendRelayUseCase(RelayType.RELAY_2, false) } returns true

            viewModel.onRelay2Clicked()
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isRelay2On)
            assertEquals("Relé 2 ligado", viewModel.uiState.value.message)
        }

    @Test
    fun givenRelay2IsOn_whenOnRelay2Clicked_thenRelay2IsTurnedOffAndMessageIsSet() =
        runTest(testDispatcher) {
            coEvery { sendRelayUseCase(RelayType.RELAY_2, false) } returns true
            viewModel.onRelay2Clicked()
            advanceUntilIdle()

            coEvery { sendRelayUseCase(RelayType.RELAY_2, true) } returns false
            viewModel.onRelay2Clicked()
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isRelay2On)
            assertEquals("Relé 2 desligado", viewModel.uiState.value.message)
        }

    @Test
    fun givenSendRelayCommandThrowsWithNoMessage_whenOnRelay2Clicked_thenFallbackErrorMessageIsSet() =
        runTest(testDispatcher) {
            coEvery { sendRelayUseCase(RelayType.RELAY_2, false) } throws RuntimeException()

            viewModel.onRelay2Clicked()
            advanceUntilIdle()

            assertEquals("Erro ao alternar Relé 2", viewModel.uiState.value.message)
        }

    @Test
    fun givenIdleState_whenBleStateChangesToScanning_thenIsLoadingIsTrue() =
        runTest(testDispatcher) {
            connectionStateFlow.value = BleConnectionState.Scanning
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isLoading)
            assertEquals(BleConnectionState.Scanning, viewModel.uiState.value.connectionState)
        }

    @Test
    fun givenIdleState_whenBleStateChangesToConnecting_thenIsLoadingIsTrue() =
        runTest(testDispatcher) {
            connectionStateFlow.value = BleConnectionState.Connecting
            advanceUntilIdle()

            assertTrue(viewModel.uiState.value.isLoading)
            assertEquals(BleConnectionState.Connecting, viewModel.uiState.value.connectionState)
        }

    @Test
    fun givenScanningState_whenBleStateChangesToConnected_thenIsLoadingIsFalse() =
        runTest(testDispatcher) {
            connectionStateFlow.value = BleConnectionState.Connected("ESP32_BLE")
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isLoading)
            assertEquals(
                BleConnectionState.Connected("ESP32_BLE"),
                viewModel.uiState.value.connectionState
            )
        }

    @Test
    fun givenRelaysAreOn_whenBleStateChangesToDisconnected_thenRelaysAreReset() =
        runTest(testDispatcher) {
            coEvery { sendRelayUseCase(RelayType.RELAY_1, false) } returns true
            viewModel.onRelay1Clicked()
            advanceUntilIdle()

            connectionStateFlow.value = BleConnectionState.Disconnected
            advanceUntilIdle()

            assertFalse(viewModel.uiState.value.isRelay1On)
            assertFalse(viewModel.uiState.value.isRelay2On)
        }

    @Test
    fun givenAnyBleState_whenBleStateChangesToError_thenConnectionStateIsUpdatedAndIsLoadingIsFalse() =
        runTest(testDispatcher) {
            connectionStateFlow.value = BleConnectionState.Error("Timeout")
            advanceUntilIdle()

            assertEquals(
                BleConnectionState.Error("Timeout"),
                viewModel.uiState.value.connectionState
            )
            assertFalse(viewModel.uiState.value.isLoading)
        }
}