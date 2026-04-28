package com.example.bleconnect.di

import com.example.shared.domain.usecase.ConnectBleDeviceUseCase
import com.example.shared.domain.usecase.DisconnectBleDeviceUseCase
import com.example.shared.domain.usecase.ObserveBleConnectionStateUseCase
import com.example.shared.domain.usecase.SendRelayCommandUseCase
import org.koin.dsl.module

val useCaseModule = module {
    factory { ConnectBleDeviceUseCase(repository = get()) }
    factory { DisconnectBleDeviceUseCase(repository = get()) }
    factory { ObserveBleConnectionStateUseCase(repository = get()) }
    factory { SendRelayCommandUseCase(repository = get()) }
}