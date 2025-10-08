package com.maulik.android_boilerplate

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

@HiltAndroidApp
class DictionaryApp : Application() {

    override fun onCreate() {
        super.onCreate()
        
        // Initialize Timber for logging
        // Only plant debug tree in debug builds, so no logs in release
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
