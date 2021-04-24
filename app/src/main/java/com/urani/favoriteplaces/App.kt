package com.urani.favoriteplaces

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class App : Application(){

    companion object {
        lateinit var app: App

        fun getInstance(): App {
            return app
        }
    }
    override fun onCreate() {
        super.onCreate()

        app = this
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        Timber.plant(Timber.DebugTree())
    }
}