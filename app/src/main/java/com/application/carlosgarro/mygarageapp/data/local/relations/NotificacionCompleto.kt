package com.application.carlosgarro.mygarageapp.data.local.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.application.carlosgarro.mygarageapp.data.local.entity.NotificacionEntity
import com.application.carlosgarro.mygarageapp.data.local.entity.ReglaMantenimientoEntity

data class NotificacionCompleto(

    @Embedded val notificacionCompleto: NotificacionEntity,

    @Relation(
        parentColumn = "vehiculoPersonalId",
        entityColumn = "id",
    )
    val vehiculoPersonal: VehiculoPersonalCompleto,

    @Relation(
        parentColumn = "reglaMantenimientoId",
        entityColumn = "id",
    )
    val reglaMantenimiento: ReglaMantenimientoEntity
)
