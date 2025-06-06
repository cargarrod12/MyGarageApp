package com.application.carlosgarro.mygarageapp.domain.model.notificacion

import com.application.carlosgarro.mygarageapp.core.enums.TipoServicio
import com.application.carlosgarro.mygarageapp.data.local.entity.NotificacionEntity

data class NotificacionModel(
    val id: Long? = 0L,
    val vehiculoPersonalId: Long = 0L,
    val reglaMantenimientoId: Long = 0L,
    val tipoServicio: TipoServicio,
    val kilometrosUltimoServicio: Int = 0,
    val kilometrosProximoServicio: Int = 0,
    val notificado: Boolean = false,
    var activo: Boolean = true,

)

fun NotificacionModel.toEntity(): NotificacionEntity {
    return NotificacionEntity(
        id = id?: 0L,
        vehiculoPersonalId = vehiculoPersonalId,
        reglaMantenimientoId = reglaMantenimientoId,
        tipoServicio = tipoServicio,
        kilometrosUltimoServicio = kilometrosUltimoServicio,
        kilometrosProximoServicio = kilometrosProximoServicio,
        notificado = notificado,
        activo = activo
    )
}

fun NotificacionEntity.toModel(): NotificacionModel {
    return NotificacionModel(
        id = id,
        vehiculoPersonalId = vehiculoPersonalId,
        reglaMantenimientoId = reglaMantenimientoId,
        tipoServicio = tipoServicio,
        kilometrosUltimoServicio = kilometrosUltimoServicio,
        kilometrosProximoServicio = kilometrosProximoServicio,
        notificado = notificado,
        activo = activo
    )
}




