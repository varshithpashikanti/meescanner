package com.appgolive.meescanner

import android.app.Application
import com.agl.ml.di.initKoin
import org.koin.android.ext.koin.androidContext

class MLApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        initKoin {

            androidContext(this@MLApplication)

        }
    }
}