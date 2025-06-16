package com.application.carlosgarro.mygarageapp.domain.model.notificacion.usecases

import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel
import com.application.carlosgarro.mygarageapp.domain.repository.NotificacionRepository

class getNotificacionesByUserToNotify(
    private val repository: NotificacionRepository
) {

    suspend operator fun invoke(userEmail: String, vehiculo: Long): List<NotificacionModel> {
        return try {
            repository.getNotificacionesByUserToNotify(userEmail, vehiculo)
        }catch (e: Exception) {
            emptyList<NotificacionModel>()
        }
    }



}