package com.application.carlosgarro.mygarageapp.core.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.application.carlosgarro.mygarageapp.core.worker.revisiones.ProximasRevisionesWorker
import com.application.carlosgarro.mygarageapp.core.worker.sincronizacion.SincronizacionDatosWorker
import java.time.Duration
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.concurrent.TimeUnit

object BatchScheduler {

    fun schedule(context: Context) {

        val sincronzacionRequest = OneTimeWorkRequestBuilder<SincronizacionDatosWorker>()
            .setInitialDelay(15, TimeUnit.HOURS)
            .build()

        WorkManager.getInstance(context).enqueueUniqueWork(
            "sincronizacion_worker",
            ExistingWorkPolicy.REPLACE,
            sincronzacionRequest
        )



        val workManager = WorkManager.getInstance(context)

        val delay = calcularDelayInicial()


        val revisionRequest = PeriodicWorkRequestBuilder<ProximasRevisionesWorker>(
            2, TimeUnit.DAYS
        )
            .setInitialDelay(delay, TimeUnit.MILLISECONDS)
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.NOT_REQUIRED)
                    .build()
            )
            .build()

        workManager.enqueueUniquePeriodicWork(
            "revisiones_worker",
            ExistingPeriodicWorkPolicy.UPDATE,
            revisionRequest
        )
    }

    private fun calcularDelayInicial(): Long {
        val zona = ZoneId.systemDefault()
        val ahora = LocalDateTime.now(zona)
        val proximaEjecucion = ahora.withHour(12).withMinute(0).withSecond(0).withNano(0)
            .let {
                if (ahora >= it) it.plusDays(2) else it
            }

        return Duration.between(ahora, proximaEjecucion).toMillis()
    }
}