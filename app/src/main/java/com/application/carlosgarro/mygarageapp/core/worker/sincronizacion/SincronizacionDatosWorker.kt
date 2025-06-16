package com.application.carlosgarro.mygarageapp.core.worker.sincronizacion

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.application.carlosgarro.mygarageapp.data.external.firestore.FirestoreService
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject


@HiltWorker
class SincronizacionDatosWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val firestoreService: FirestoreService,
    private val auth: FirebaseAuth
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {

        Log.d("SincronizacionDatosWorker", "Starting data synchronization...")
        return try {
            val userId = auth.currentUser?.uid
            val email = auth.currentUser?.email
            if (userId.isNullOrEmpty() || email.isNullOrEmpty()) {
                firestoreService.synDatosGenerales()
                return Result.success()
            }
            firestoreService.synDatos(userId, email)
            Log.d("SincronizacionDatosWorker", "finish data synchronization...")
            Result.success()
        } catch (e: Exception) {
            Log.e("SincronizacionDatosWorker", "Error during data synchronization: ${e.message}")
            e.printStackTrace()
            Result.failure()
        }
    }
}