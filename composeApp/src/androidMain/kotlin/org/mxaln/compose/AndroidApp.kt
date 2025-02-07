package org.mxaln.compose

import android.app.Application
import org.koin.android.ext.koin.androidContext
import org.mxaln.compose.di.initKoin

class AndroidApp : Application() {
    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@AndroidApp)
        }
    }
}