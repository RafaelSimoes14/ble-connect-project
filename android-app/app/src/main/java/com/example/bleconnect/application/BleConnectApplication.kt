package com.example.bleconnect.application

import android.app.Application
import com.example.bleconnect.di.appModules
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext.startKoin

class BleConnectApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@BleConnectApplication)
            modules(appModules)
        }
    }
}