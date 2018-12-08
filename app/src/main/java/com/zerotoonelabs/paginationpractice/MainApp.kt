package com.zerotoonelabs.paginationpractice

import android.app.Application
import com.zerotoonelabs.paginationpractice.di.appModule
import org.koin.android.ext.android.startKoin

class MainApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin(this, listOf(appModule))
    }
}