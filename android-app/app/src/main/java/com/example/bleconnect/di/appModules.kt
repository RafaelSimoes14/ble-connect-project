package com.example.bleconnect.di

import org.koin.core.module.Module

val appModules: List<Module> = listOf(
    repositoryModule,
    sharedBleModule,
    useCaseModule,
    presentationModule
)