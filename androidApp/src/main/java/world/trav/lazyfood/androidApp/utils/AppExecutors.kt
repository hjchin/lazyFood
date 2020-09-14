package world.trav.lazyfood.androidApp.utils

//
// Created by  on 13/9/20.
//

import android.os.Handler
import android.os.Looper
import androidx.annotation.NonNull
import java.util.concurrent.Executor
import java.util.concurrent.Executors

class AppExecutors internal constructor(diskIO:Executor, networkIO:Executor, mainThread:Executor) {

    private val diskIO : Executor = diskIO
    private val networkIO: Executor = networkIO
    private val mainThread: Executor = mainThread

    constructor() : this(DiskIOThreadExecutor(), Executors.newFixedThreadPool(THREAD_COUNT),
        MainThreadExecutor())

    fun diskIO():Executor {
        return diskIO
    }
    fun networkIO():Executor {
        return networkIO
    }
    fun mainThread():Executor {
        return mainThread
    }
    private class MainThreadExecutor:Executor {
        private val mainThreadHandler = Handler(Looper.getMainLooper())
        override fun execute(@NonNull command:Runnable) {
            mainThreadHandler.post(command)
        }
    }
    companion object {
        private val THREAD_COUNT = 3
        val instance by lazy {
            AppExecutors()
        }
    }
}