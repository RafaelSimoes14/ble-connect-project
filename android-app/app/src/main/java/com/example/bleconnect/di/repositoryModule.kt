package com.example.bleconnect.di

import com.example.shared.data.BleConnectRepositoryImpl
import com.example.shared.domain.repository.BleConnectRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<BleConnectRepository> {
        BleConnectRepositoryImpl(bleController = get())
    }
}