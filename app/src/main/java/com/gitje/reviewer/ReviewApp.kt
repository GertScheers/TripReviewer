package com.gitje.reviewer

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class ReviewApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initDI()
    }


    private fun initDI() {
        startKoin {
            androidLogger()
            androidContext(this@ReviewApp)
            modules(appModule)
        }
    }
}