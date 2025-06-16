package com.application.carlosgarro.mygarageapp

import android.app.Application
import android.util.Log
import androidx.work.Configuration
import com.application.carlosgarro.mygarageapp.core.worker.MyGarageWorkerFactory
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject


@HiltAndroidApp
class MyApp : Application(), Configuration.Provider{

    @Inject
    lateinit var workerFactory: MyGarageWorkerFactory

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setMinimumLoggingLevel(Log.DEBUG)
            .setWorkerFactory(workerFactory)
            .build()

}
