package com.application.carlosgarro.mygarageapp.core.worker.revisiones


import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.application.carlosgarro.mygarageapp.core.enums.EstadoVehiculo
import com.application.carlosgarro.mygarageapp.core.worker.BatchScheduler
import com.application.carlosgarro.mygarageapp.core.worker.Notifier
import com.application.carlosgarro.mygarageapp.domain.model.Resource
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases.getNotificacionesByUserToNotify
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.VehiculoPersonalModel
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.getVehiculoPersonalByUserUseCase
import com.application.carlosgarro.mygarageapp.domain.model.vehiculopersonal.usecases.saveVehiculoPersonalUseCase
import com.google.firebase.auth.FirebaseAuth
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import java.time.LocalDate


@HiltWorker
class ProximasRevisionesWorker @AssistedInject constructor(
    @Assisted context: Context,
    @Assisted workerParams: WorkerParameters,
    private val firebaseAuth: FirebaseAuth,
    private val revisionesUseCase: getNotificacionesByUserToNotify,
    private val vehiculosUseCase: getVehiculoPersonalByUserUseCase,
    private val guardarVehiculo: saveVehiculoPersonalUseCase,
    private val notifier: Notifier
): CoroutineWorker(context, workerParams) {


    override suspend fun doWork(): Result {
        var error = true;
        val fechaActual = LocalDate.now()
        val email = firebaseAuth.currentUser?.email ?: ""
        if (email.isEmpty()) {
            return Result.success()
        }
        try {

            vehiculosUseCase(email).collect() { resource ->
                when (resource) {
                    is Resource.Loading -> {
                        Log.i("PROCESO PROXIMAS REVISIONES", "Loading vehiculos")
                    }

                    is Resource.Success -> {
                        val vehiculos = resource.data ?: emptyList()
                        if (vehiculos.isEmpty()) {
                            Log.i(
                                "PROCESO PROXIMAS REVISIONES",
                                "No vehicles found for user: $email"
                            )
                            error = true
                            return@collect
                        }
                        procesarVehiculos(vehiculos, fechaActual, email)
                        Log.i("PROCESO PROXIMAS REVISIONES", "Success: ${resource.data}")
                    }

                    is Resource.Error -> {
                        Log.e("RESUMEN", "Error: ${resource.message}")
                    }
                }

            }
        }catch (e: Exception) {
            Log.e("PROCESO PROXIMAS REVISIONES", "Error processing vehicles: ${e.message}")
            error = true
        }

        BatchScheduler.schedule(applicationContext)
        if (error) {
            Log.e("PROXIMAS REVISIONES", "Error retrieving vehicles for user: $email")
            return Result.failure()
        }
        return Result.success()
    }

    private suspend fun procesarVehiculos(vehiculos: List<VehiculoPersonalModel>, fechaActual: LocalDate, email: String) {
        Log.i("PROCESO PROXIMAS REVISIONES", "Processing vehicles for user: $email")
        for (vehiculo in vehiculos) {

            //Comprobamos que el vehiculo tenga se haya actualizado en las últimas 3 semanas
            if(vehiculo.fechaUltModificacion?.isBefore(fechaActual.minusWeeks(3)) == true){
                Log.i("PROCESO PROXIMAS REVISIONES", "Vehicle ${vehiculo.modelo} has not been updated in the last 3 weeks")
                    notifier.sendNotification(
                        title = "Vehículo sin actualizar",
                        message = "¿Has usado tu vehículo últimamente? La información de tu ${vehiculo.modelo} no se ha actualizado desde hace varias semanas.",
                    )
                break
            }
            //Obtenemos sus notificaciones activas y proximas (menos de 1000Km)
            val revisionesVehiculo = revisionesUseCase(email,vehiculo.id!!)

            //Generamos el mensaje de notificación
            val mensajeNotificacion = procesarMensajeNotificaciones(revisionesVehiculo ,vehiculo)
            if (mensajeNotificacion.isNotEmpty()) {
                Log.i("PROCESO PROXIMAS REVISIONES", "Vehicle ${vehiculo.modelo} has notifications: $mensajeNotificacion")
                notifier.sendNotification(
                    title = "Próximas revisiones para ${vehiculo.modelo}:",
                    message = mensajeNotificacion,
                )
                guardarVehiculo(
                    vehiculo.copy(
                        fechaUltModificacion = LocalDate.now(),
                        estado = EstadoVehiculo.REVISAR
                    )
                )
            } else {
                Log.i("PROCESO PROXIMAS REVISIONES", "No notifications for vehicle: ${vehiculo.modelo}")
            }
        }

    }

    private fun procesarMensajeNotificaciones(
        listaNotificaciones: List<NotificacionModel>,
        vehiculo: VehiculoPersonalModel
    ): String {
        var mensajeNotificacion = ""
        for (notificacion in listaNotificaciones) {
            mensajeNotificacion += "- ${notificacion.tipoServicio} a los ${notificacion.kilometrosProximoServicio}Km.\n"
            Log.i("PROCESO PROXIMAS REVISIONES", "Notification: ${notificacion.tipoServicio} for vehicle: ${vehiculo.modelo} on: ${notificacion.kilometrosProximoServicio}Km")
        }
        return mensajeNotificacion.trim()
    }
}


