package com.example.bleconnect.di

import com.example.bleconnect.presentation.BleConnectViewModel
import org.koin.dsl.module

val presentationModule = module {
    factory {
        BleConnectViewModel(
            connectBleDeviceUseCase = get(),
            disconnectBleDeviceUseCase = get(),
            observeBleConnectionStateUseCase = get(),
            sendRelayCommandUseCase = get()
        )
    }
}