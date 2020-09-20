package world.trav.lazyfood.androidApp

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

//
// Created by  on 7/9/20.
//
@HiltAndroidApp
internal class LazyFoodApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}