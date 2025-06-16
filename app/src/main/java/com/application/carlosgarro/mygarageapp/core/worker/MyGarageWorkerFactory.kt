package com.application.carlosgarro.mygarageapp.core.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.application.carlosgarro.mygarageapp.core.worker.revisiones.ProximasRevisionesWorker
import com.application.carlosgarro.mygarageapp.core.worker.sincronizacion.SincronizacionDatosWorker
import com.application.carlosgarro.mygarageapp.data.external.firestore.FirestoreService
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.getNotificacionesByUserToNotify
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoPersonalByUserUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.saveVehiculoPersonalUseCase
import com.google.firebase.auth.FirebaseAuth
import javax.inject.Inject

class MyGarageWorkerFactory @Inject constructor(
    private val firebaseAuth: FirebaseAuth,
    private val revisionesUseCase: getNotificacionesByUserToNotify,
    private val vehiculosUseCase: getVehiculoPersonalByUserUseCase,
    private val notifier: Notifier,
    private val firestoreService: FirestoreService,
    private val saveVehiculoPersonalUseCase: saveVehiculoPersonalUseCase
): WorkerFactory() {

    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker? {
        return when (workerClassName) {
            ProximasRevisionesWorker::class.java.name -> {
                ProximasRevisionesWorker(appContext, workerParameters, firebaseAuth, revisionesUseCase, vehiculosUseCase,saveVehiculoPersonalUseCase, notifier)
            }
            SincronizacionDatosWorker::class.java.name -> {
                SincronizacionDatosWorker(appContext, workerParameters, firestoreService, firebaseAuth)
            }
            else -> null
        }
    }
}