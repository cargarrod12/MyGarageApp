package com.application.carlosgarro.mygarageapp.data.local.relations

import androidx.room.Embedded
import com.application.carlosgarro.mygarageapp.data.local.entity.NotificacionEntity

data class NotificacionCompleto(

    @Embedded val notificacionCompleto: NotificacionEntity,

//    @Relation(
//        parentColumn = "vehiculoPersonalId",
//        entityColumn = "id",
//    )
//    val vehiculoPersonal: VehiculoPersonalCompleto,

//    @Relation(
//        parentColumn = "reglaMantenimientoId",
//        entityColumn = "id",
//    )
//    val reglaMantenimiento: ReglaMantenimientoEntity
)
