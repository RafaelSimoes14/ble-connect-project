package com.example.bleconnect.domain

import com.example.shared.domain.repository.BleConnectRepository
import com.example.shared.domain.usecase.DisconnectBleDeviceUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Test

class DisconnectBleDeviceUseCaseTest {
    private val repository: BleConnectRepository = mockk()
    private val useCase = DisconnectBleDeviceUseCase(repository)

    @Test
    fun givenConnectedDevice_whenInvoked_thenRepositoryDisconnectIsCalled() = runTest {
        coEvery { repository.disconnect() } returns Unit

        useCase()

        coVerify(exactly = 1) { repository.disconnect() }
    }
}