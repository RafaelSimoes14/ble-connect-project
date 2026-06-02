package com.example.bleconnect.domain

import com.example.shared.domain.repository.BleConnectRepository
import com.example.shared.domain.usecase.ConnectBleDeviceUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class ConnectBleDeviceUseCaseTest {
    private val repository: BleConnectRepository = mockk()
    private val useCase = ConnectBleDeviceUseCase(repository)

    @Test
    fun givenDeviceName_whenInvoked_thenRepositoryConnectIsCalledWithDeviceName() = runTest {
        coEvery { repository.connect(any()) } returns Unit

        useCase("ESP32_BLE")

        coVerify(exactly = 1) { repository.connect("ESP32_BLE") }
    }
}