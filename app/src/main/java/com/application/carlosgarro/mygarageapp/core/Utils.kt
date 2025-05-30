package com.application.carlosgarro.mygarageapp.core

import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel

fun calcularProximosMantenimientos(notificaciones: List<NotificacionModel>, kilometros: Int): List<NotificacionModel> {
    val proximosMantenimientos = mutableListOf<NotificacionModel>()

    for (notificacion in notificaciones) {
        if (notificacion.kilometrosProximoServicio - kilometros < 20000) {
            proximosMantenimientos.add(notificacion)
        }
    }

    return proximosMantenimientos
}