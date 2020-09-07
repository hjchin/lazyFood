package world.trav.lazyfood.androidApp

import android.app.Application
import timber.log.Timber

//
// Created by  on 7/9/20.
//
internal class LazyFoodApp : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}