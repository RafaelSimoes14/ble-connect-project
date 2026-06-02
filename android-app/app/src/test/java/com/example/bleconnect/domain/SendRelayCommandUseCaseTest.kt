package com.example.bleconnect.domain

import com.example.shared.domain.model.RelayType
import com.example.shared.domain.repository.BleConnectRepository
import com.example.shared.domain.usecase.SendRelayCommandUseCase
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.test.runTest
import org.junit.Test

class SendRelayCommandUseCaseTest {
    private val repository: BleConnectRepository = mockk()
    private val useCase = SendRelayCommandUseCase(repository)

    @Test
    fun givenRelay1IsOff_whenInvoked_thenTurnOnRelayIsCalledAndTrueIsReturned() = runTest {
        coEvery { repository.turnOnRelay(RelayType.RELAY_1) } returns Unit

        val result = useCase(RelayType.RELAY_1, isCurrentlyOn = false)

        assertTrue(result)
        coVerify(exactly = 1) { repository.turnOnRelay(RelayType.RELAY_1) }
        coVerify(exactly = 0) { repository.turnOffRelay(any()) }
    }

    @Test
    fun givenRelay1IsOn_whenInvoked_thenTurnOffRelayIsCalledAndFalseIsReturned() = runTest {
        coEvery { repository.turnOffRelay(RelayType.RELAY_1) } returns Unit

        val result = useCase(RelayType.RELAY_1, isCurrentlyOn = true)

        assertFalse(result)
        coVerify(exactly = 1) { repository.turnOffRelay(RelayType.RELAY_1) }
        coVerify(exactly = 0) { repository.turnOnRelay(any()) }
    }

    @Test
    fun givenRelay2IsOff_whenInvoked_thenTurnOnRelayIsCalledAndTrueIsReturned() = runTest {
        coEvery { repository.turnOnRelay(RelayType.RELAY_2) } returns Unit

        val result = useCase(RelayType.RELAY_2, isCurrentlyOn = false)

        assertTrue(result)
        coVerify(exactly = 1) { repository.turnOnRelay(RelayType.RELAY_2) }
        coVerify(exactly = 0) { repository.turnOffRelay(any()) }
    }

    @Test
    fun givenRelay2IsOn_whenInvoked_thenTurnOffRelayIsCalledAndFalseIsReturned() = runTest {
        coEvery { repository.turnOffRelay(RelayType.RELAY_2) } returns Unit

        val result = useCase(RelayType.RELAY_2, isCurrentlyOn = true)

        assertFalse(result)
        coVerify(exactly = 1) { repository.turnOffRelay(RelayType.RELAY_2) }
        coVerify(exactly = 0) { repository.turnOnRelay(any()) }
    }
}