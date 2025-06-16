package com.application.carlosgarro.mygarageapp.domain.repository

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.domain.model.notificacion.NotificacionModel

interface NotificacionRepository {

    suspend fun getNotificacionByVehiculoPersonalAndTipoServicio(vehiculoPersonalId: Long, tipoServicio: TipoServicio): NotificacionModel?

    suspend fun saveNotificacion(notificacion: NotificacionModel): Boolean

    suspend fun getNotificacionesByVehiculoPersonalId(vehiculoPersonalId: Long): List<NotificacionModel>

    suspend fun updateNotificaciones(notificaciones: List<NotificacionModel>): Boolean?

    suspend fun getNotificacionesByUserToNotify(userEmail: String, vehiculos: Long): List<NotificacionModel>


}
