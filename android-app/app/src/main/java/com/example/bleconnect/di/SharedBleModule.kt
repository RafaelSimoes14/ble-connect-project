package com.example.bleconnect.di

import com.example.shared.AndroidBleController
import com.example.shared.BleController
import org.koin.dsl.module

val sharedBleModule = module {
    single<BleController> { AndroidBleController() }
}